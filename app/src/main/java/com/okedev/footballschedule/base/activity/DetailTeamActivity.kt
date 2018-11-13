package com.okedev.footballschedule.base.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.okedev.footballschedule.R
import com.okedev.footballschedule.base.adapter.DetailTeamViewPagerAdapter
import com.okedev.footballschedule.base.fragment.TeamOverviewFragment
import com.okedev.footballschedule.base.fragment.TeamPlayerFragment
import com.okedev.footballschedule.base.presenter.DetailTeamPresenter
import com.okedev.footballschedule.base.view.DetailTeamView
import com.okedev.footballschedule.data.TeamData
import com.okedev.footballschedule.data.db.database
import com.okedev.footballschedule.data.network.repository.EventRepository
import com.okedev.footballschedule.data.network.response.Team
import com.okedev.footballschedule.utils.invisible
import com.okedev.footballschedule.utils.visible
import kotlinx.android.synthetic.main.activity_detail_team.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.design.snackbar

class DetailTeamActivity : AppCompatActivity(), DetailTeamView {
    private lateinit var team: Team
    private lateinit var detailTeamPresenter: DetailTeamPresenter

    private var activityIsActive = false
    private var isFavorite: Boolean = false
    private var isLogoShow = true
    private var maxScrollSize: Int = 0
    private var menuItem: Menu? = null
    private var percentageAnimateLogo = 20
    private var teamId: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_team)

        if (intent.extras == null) {
            finish()
        }

        teamId = intent.getIntExtra("teamId", 0)
        if (teamId == 0) {
            finish()
        }

        handlingLogoCollapse()

        setSupportActionBar(detail_team_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        detailTeamPresenter = DetailTeamPresenter(this, EventRepository())
        detailTeamPresenter.getTeam(teamId)
    }

    override fun onStart() {
        super.onStart()
        activityIsActive = true
    }

    override fun onPause() {
        super.onPause()
        detailTeamPresenter.detachProcess()
        activityIsActive = false
    }

    override fun onStop() {
        super.onStop()
        detailTeamPresenter.detachProcess()
        activityIsActive = false
    }

    override fun onDestroy() {
        super.onDestroy()
        detailTeamPresenter.detachProcess()
        activityIsActive = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite, menu)
        menuItem = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                if (this::team.isInitialized) {
                    teamFavorite()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showData(data: Team) {
        hideLoading()
        team = data

        detail_team_name.text = team.strTeam
        detail_team_year.text = team.intFormedYear
        detail_team_stadium.text = team.strStadium

        if (activityIsActive) {
            if (team.strTeamBadge.isNullOrEmpty()) {
                detail_team_logo.setImageResource(R.drawable.ic_team_white)
            } else {
                Glide.with(this).load(team.strTeamBadge).into(detail_team_logo)
            }
        }

        setupViewPager(detail_team_viewpager)
        detail_team_tab?.setupWithViewPager(detail_team_viewpager)

        detailTeamPresenter.favoriteTeam(database, team.idTeam.toString())
        menuItem?.findItem(R.id.add_to_favorite)?.isVisible = true

        EventBus.getDefault().post(TeamData(team))
    }

    override fun showLoading() {
        detail_team_progressbar?.visible()
    }

    override fun hideLoading() {
        detail_team_progressbar?.invisible()
    }

    override fun dataNotFound(str: String?) {
        hideLoading()
        detail_team_name.text = str
    }

    private fun handlingLogoCollapse() {
        detail_team_appbar?.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (maxScrollSize == 0)
                maxScrollSize = appBarLayout.totalScrollRange

            val percentage = Math.abs(verticalOffset) * 100 / maxScrollSize

            if (percentage >= percentageAnimateLogo && isLogoShow) {
                isLogoShow = false
                detail_team_logo.animate()
                        .scaleY(0f).scaleX(0f)
                        .setDuration(200)
                        .start()
            }

            if (percentage <= percentageAnimateLogo && !isLogoShow) {
                isLogoShow = true
                detail_team_logo.animate()
                        .scaleY(1f).scaleX(1f)
                        .start()
            }

            if (percentage >= 50) {
                if (this::team.isInitialized) {
                    supportActionBar?.title = team.strTeam
                }
            } else {
                supportActionBar?.title = ""
            }
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = DetailTeamViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(TeamOverviewFragment(), "OVERVIEW")
        adapter.addFragment(TeamPlayerFragment.newInstance(team.idTeam?.toInt()), "PLAYER")
        viewPager.adapter = adapter
        viewPager.adapter?.notifyDataSetChanged()
    }

    override fun showSnackBar(m: String) {
        team_layout.snackbar(m).show()
    }

    override fun setFavorite(b: Boolean) {
        isFavorite = b

        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_bold)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star)
    }

    private fun teamFavorite() {
        if (isFavorite) {
            detailTeamPresenter.removeFromFavorite(database, teamId.toString())
        }
        else {
            detailTeamPresenter.addToFavorite(database, team)
        }
    }

}