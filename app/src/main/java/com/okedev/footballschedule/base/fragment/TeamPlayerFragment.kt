package com.okedev.footballschedule.base.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.okedev.footballschedule.R
import com.okedev.footballschedule.base.activity.DetailPlayerActivity
import com.okedev.footballschedule.base.adapter.TeamPlayerAdapter
import com.okedev.footballschedule.base.presenter.TeamPlayerPresenter
import com.okedev.footballschedule.base.view.TeamPlayerView
import com.okedev.footballschedule.data.network.repository.EventRepository
import com.okedev.footballschedule.data.network.response.Player
import com.okedev.footballschedule.data.network.response.PlayerResponse
import com.okedev.footballschedule.utils.gone
import com.okedev.footballschedule.utils.invisible
import com.okedev.footballschedule.utils.visible
import kotlinx.android.synthetic.main.fragment_team_player.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.onRefresh


class TeamPlayerFragment : Fragment(), TeamPlayerView {

    private var teamId: Int? = 0
    private lateinit var teamPlayerPresenter: TeamPlayerPresenter
    private var players: MutableList<Player> = mutableListOf()

    companion object {
        fun newInstance(i: Int?): TeamPlayerFragment {
            val fragment = TeamPlayerFragment()
            fragment.teamId = i
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_player, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        teamPlayerPresenter = TeamPlayerPresenter(this, EventRepository())

        player_recycler.layoutManager = LinearLayoutManager(context)
        player_recycler.adapter = TeamPlayerAdapter(players) {
            context?.startActivity<DetailPlayerActivity>("playerId" to it.idPlayer?.toInt())
        }

        getData(teamId)

        player_swipe_refresh?.onRefresh {
            getData(teamId)
        }
    }

    private fun getData(l: Int?) {
        teamPlayerPresenter.getData(l)
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

    override fun showData(data: PlayerResponse?) {
        hideLoading()
        val activity = activity
        if (activity != null && isAdded) {
            data?.player?.let {
                players.clear()
                for (player: Player in it) {
                    players.add(player)
                }
                hideLoading()
                player_recycler?.adapter?.notifyDataSetChanged()
            }?: run {
                dataNotFound(resources.getString(R.string.data_empty))
            }
        }
    }

}
