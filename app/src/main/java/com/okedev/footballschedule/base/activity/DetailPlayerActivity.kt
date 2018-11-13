package com.okedev.footballschedule.base.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.okedev.footballschedule.R
import com.okedev.footballschedule.base.presenter.DetailPlayerPresenter
import com.okedev.footballschedule.base.view.DetailPlayerView
import com.okedev.footballschedule.data.db.database
import com.okedev.footballschedule.data.network.repository.EventRepository
import com.okedev.footballschedule.data.network.response.Player
import com.okedev.footballschedule.utils.formatDate
import com.okedev.footballschedule.utils.invisible
import com.okedev.footballschedule.utils.strToDate
import com.okedev.footballschedule.utils.visible
import kotlinx.android.synthetic.main.activity_detail_player.*
import org.jetbrains.anko.design.snackbar

class DetailPlayerActivity : AppCompatActivity(), DetailPlayerView {

    private lateinit var player: Player
    private lateinit var detailPlayerPresenter: DetailPlayerPresenter

    private var activityIsActive = false
    private var isFavorite: Boolean = false
    private var menuItem: Menu? = null
    private var playerId: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_player)

        if (intent.extras == null) {
            finish()
        }

        playerId = intent.getIntExtra("playerId", 0)
        if (playerId == 0) {
            finish()
        }

        detailPlayerPresenter = DetailPlayerPresenter(this, EventRepository())
        detailPlayerPresenter.getData(playerId)
    }

    override fun onStart() {
        super.onStart()
        activityIsActive = true
    }

    override fun onPause() {
        super.onPause()
        detailPlayerPresenter.detachProcess()
        activityIsActive = false
    }

    override fun onStop() {
        super.onStop()
        detailPlayerPresenter.detachProcess()
        activityIsActive = false
    }

    override fun onDestroy() {
        super.onDestroy()
        detailPlayerPresenter.detachProcess()
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
                if (this::player.isInitialized) {
                    playerFavorite()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showData(data: Player) {
        hideLoading()
        player = data

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = player.strPlayer

        player_layout.visible()
        player_weight.text = player.strWeight
        player_height.text = player.strHeight
        player_position.text = player.strPosition
        player_nationality.text = player.strNationality
        player_borndate.text = formatDate(strToDate(player.dateBorn),"dd/mm/YYYY")
        player_description.text = player.strDescriptionEN

        if (activityIsActive) {
            if (!player.strBanner.isNullOrEmpty()) {
                Glide.with(this).load(player.strBanner).into(player_banner)
            } else if (!player.strFanart1.isNullOrEmpty()) {
                Glide.with(this).load(player.strFanart1).into(player_banner)
            }
        }


        detailPlayerPresenter.favoritePlayer(database, player.idPlayer.toString())
        menuItem?.findItem(R.id.add_to_favorite)?.isVisible = true
    }

    override fun showLoading() {
        detail_player_progressbar?.visible()
    }

    override fun hideLoading() {
        detail_player_progressbar?.invisible()
    }

    override fun dataNotFound(str: String?) {
        hideLoading()
        detail_player_message.text = str
    }

    override fun showSnackBar(m: String) {
        player_layout.snackbar(m).show()
    }

    override fun setFavorite(b: Boolean) {
        isFavorite = b

        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_bold)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star)
    }

    private fun playerFavorite() {
        if (isFavorite) {
            detailPlayerPresenter.removeFromFavorite(database, playerId.toString())
        }
        else {
            detailPlayerPresenter.addToFavorite(database, player)
        }
    }
}
