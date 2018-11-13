package com.okedev.footballschedule.base.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.okedev.footballschedule.R
import com.okedev.footballschedule.base.activity.DetailPlayerActivity
import com.okedev.footballschedule.base.adapter.PlayerFavoriteAdapter
import com.okedev.footballschedule.base.presenter.PlayerFavoritePresenter
import com.okedev.footballschedule.base.view.PlayerFavoriteView
import com.okedev.footballschedule.data.db.model.PlayerTable
import com.okedev.footballschedule.utils.gone
import com.okedev.footballschedule.utils.invisible
import com.okedev.footballschedule.utils.visible
import kotlinx.android.synthetic.main.fragment_team_player.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.onRefresh

class PlayerFavoriteFragment : Fragment(), PlayerFavoriteView {

    private var players: MutableList<PlayerTable> = mutableListOf()

    private lateinit var presenter: PlayerFavoritePresenter
    private lateinit var adapter: PlayerFavoriteAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter = PlayerFavoritePresenter(this, context)

        player_recycler.layoutManager = LinearLayoutManager(context)
        adapter = PlayerFavoriteAdapter(players) {
            context?.startActivity<DetailPlayerActivity>("playerId" to it.playerId?.toInt())
        }

        player_recycler.adapter = adapter

        presenter.getData()

        player_swipe_refresh?.onRefresh {
            presenter.getData()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_team_player, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.getData()
    }

    override fun showLoading() {
        player_swipe_refresh?.let {
            if (!it.isRefreshing) {
                player_progressbar?.visible()
            }
        }
        player_message?.gone()
    }

    override fun hideLoading() {
        player_progressbar?.invisible()
        player_swipe_refresh?.let {
            if (it.isRefreshing) {
                it.isRefreshing = false
            }
        }
    }

    override fun dataNotFound(str: String?) {
        hideLoading()
        player_message?.let {
            it.text = if (str.isNullOrEmpty()) resources.getString(R.string.data_error) else str
            it.visible()
        }
    }

    override fun showData(result: List<PlayerTable>) {
        val activity = activity
        if (activity != null && isAdded) {
            players.clear()
            players.addAll(result)
            adapter.notifyDataSetChanged()
            if (players.size == 0) {
                dataNotFound(resources.getString(R.string.data_empty))
            }
        }
    }
}
