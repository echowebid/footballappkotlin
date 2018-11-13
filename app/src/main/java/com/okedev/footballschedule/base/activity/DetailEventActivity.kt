package com.okedev.footballschedule.base.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.okedev.footballschedule.R
import com.okedev.footballschedule.R.drawable.ic_star
import com.okedev.footballschedule.R.drawable.ic_star_bold
import com.okedev.footballschedule.R.id.add_to_favorite
import com.okedev.footballschedule.R.menu.favorite
import com.okedev.footballschedule.base.presenter.DetailEventPresenter
import com.okedev.footballschedule.base.view.DetailEventView
import com.okedev.footballschedule.data.db.database
import com.okedev.footballschedule.data.network.repository.EventRepository
import com.okedev.footballschedule.data.network.response.Event
import com.okedev.footballschedule.data.network.response.Team
import com.okedev.footballschedule.utils.formatDate
import com.okedev.footballschedule.utils.invisible
import com.okedev.footballschedule.utils.strToDate
import com.okedev.footballschedule.utils.visible
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.design.snackbar

class DetailEventActivity : AppCompatActivity(), DetailEventView {
    private lateinit var event: Event
    private lateinit var detailEventPresenter: DetailEventPresenter

    private var activityIsActive = false
    private var eventId: Int? = 0
    private var isFavorite: Boolean = false
    private var isFinished: Boolean = false
    private var menuItem: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (intent.extras == null) {
            finish()
        }

        eventId = intent.getIntExtra("eventId", 0)
        if (eventId == 0) {
            finish()
        }

        detailEventPresenter = DetailEventPresenter(this, EventRepository())
        detailEventPresenter.getEvent(eventId)
    }

    override fun onStart() {
        super.onStart()
        activityIsActive = true
    }

    override fun onPause() {
        super.onPause()
        detailEventPresenter.detachProcess()
        activityIsActive = false
    }

    override fun onStop() {
        super.onStop()
        detailEventPresenter.detachProcess()
        activityIsActive = false
    }

    override fun onDestroy() {
        super.onDestroy()
        detailEventPresenter.detachProcess()
        activityIsActive = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(favorite, menu)
        menuItem = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            add_to_favorite -> {
                if (this::event.isInitialized) {
                    eventFavorite()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showLoading() {
        progressbar.visible()
    }

    override fun hideLoading() {
        progressbar.invisible()
    }

    override fun dataNotFound(str: String?) {
        hideLoading()
        message.text = if (str.isNullOrEmpty()) resources.getString(R.string.data_error) else str
        message.visible()
    }

    override fun showEvent(data: Event) {
        hideLoading()
        
        event = data

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = event.strEvent

        isFinished = !(event.intHomeScore.isNullOrEmpty() && event.intAwayScore.isNullOrEmpty())

        val round = "Round " +  event.intRound
        val schedule = formatDate(strToDate(event.dateEvent)) + " " + event.strTime?.substring(0, 5)

        event_name.text = event.strLeague
        event_round.text = round
        event_schedule.text = schedule
        home_team.text = event.strHomeTeam
        away_team.text = event.strAwayTeam
        event_score.text = if (isFinished) event.intHomeScore + " - " + event.intAwayScore else "VS"

        if (!event.strHomeFormation.isNullOrEmpty() && !event.strAwayFormation.isNullOrEmpty()) {
            formation.visible()
            home_formation.text = event.strHomeFormation
            away_formation.text = event.strAwayFormation
        }

        if (isFinished) {
            finished.visible()

            home_goal.text = if (event.strHomeGoalDetails.isNullOrEmpty()) "-" else event.strHomeGoalDetails?.replace(";", "\n")
            home_redcard.text = if (event.strHomeRedCards.isNullOrEmpty()) "-" else event.strHomeRedCards?.replace(";", "\n")
            home_yellowcard.text = if (event.strHomeYellowCards.isNullOrEmpty()) "-" else event.strHomeYellowCards?.replace(";", "\n")
            home_goalkeeper.text = if (event.strHomeLineupGoalkeeper.isNullOrEmpty()) "-" else event.strHomeLineupGoalkeeper?.replace("; ", "\n")
            home_defense.text = if (event.strHomeLineupDefense.isNullOrEmpty()) "-" else event.strHomeLineupDefense?.replace("; ", "\n")
            home_midfield.text = if (event.strHomeLineupMidfield.isNullOrEmpty()) "-" else event.strHomeLineupMidfield?.replace("; ", "\n")
            home_forward.text = if (event.strHomeLineupForward.isNullOrEmpty()) "-" else event.strHomeLineupForward?.replace("; ", "\n")

            away_goal.text = if (event.strAwayGoalDetails.isNullOrEmpty()) "-" else event.strAwayGoalDetails?.replace(";", "\n")
            away_redcard.text = if (event.strAwayRedCards.isNullOrEmpty()) "-" else event.strAwayRedCards?.replace(";", "\n")
            away_yellowcard.text = if (event.strAwayYellowCards.isNullOrEmpty()) "-" else event.strAwayYellowCards?.replace(";", "\n")
            away_goalkeeper.text = if (event.strAwayLineupGoalkeeper.isNullOrEmpty()) "-" else event.strAwayLineupGoalkeeper?.replace("; ", "\n")
            away_defense.text = if (event.strAwayLineupDefense.isNullOrEmpty()) "-" else event.strAwayLineupDefense?.replace("; ", "\n")
            away_midfield.text = if (event.strAwayLineupMidfield.isNullOrEmpty()) "-" else event.strAwayLineupMidfield?.replace("; ", "\n")
            away_forward.text = if (event.strAwayLineupForward.isNullOrEmpty()) "-" else event.strAwayLineupForward?.replace("; ", "\n")
        }

        detailEventPresenter.getHomeTeam(event.idAwayTeam?.toInt())
        detailEventPresenter.getAwayTeam(event.idHomeTeam?.toInt())
        detailEventPresenter.favoriteEvent(database, event.idEvent.toString())

        menuItem?.findItem(R.id.add_to_favorite)?.isVisible = true
    }

    override fun showHomeTeam(team: Team) {
        if (activityIsActive) {
            if (team.strTeamBadge.isNullOrEmpty()) {
                home_logo.setImageResource(R.drawable.ic_team)
            } else {
                Glide.with(this).load(team.strTeamBadge).into(home_logo)
            }
        }
    }

    override fun showAwayTeam(team: Team) {
        if (activityIsActive) {
            if (team.strTeamBadge.isNullOrEmpty()) {
                away_logo.setImageResource(R.drawable.ic_team)
            } else {
                Glide.with(this).load(team.strTeamBadge).into(away_logo)
            }
        }
    }

    override fun showSnackBar(m: String) {
        main_layout.snackbar(m).show()
    }

    override fun setFavorite(b: Boolean) {
        isFavorite = b

        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_star_bold)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_star)
    }

    private fun eventFavorite() {
        if (isFavorite) {
            detailEventPresenter.removeFromFavorite(database, eventId.toString())
        }
        else {
            detailEventPresenter.addToFavorite(database, event)
        }
    }
}
