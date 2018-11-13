package com.okedev.footballschedule.base.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.okedev.footballschedule.R
import com.okedev.footballschedule.base.activity.DetailTeamActivity
import com.okedev.footballschedule.base.adapter.TeamFavoriteAdapter
import com.okedev.footballschedule.base.presenter.TeamFavoritePresenter
import com.okedev.footballschedule.base.view.TeamFavoriteView
import com.okedev.footballschedule.data.db.model.TeamTable
import com.okedev.footballschedule.utils.gone
import com.okedev.footballschedule.utils.invisible
import com.okedev.footballschedule.utils.visible
import kotlinx.android.synthetic.main.fragment_team.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.onRefresh

class TeamFavoriteFragment : Fragment(), TeamFavoriteView {

    private var teams: MutableList<TeamTable> = mutableListOf()

    private lateinit var presenter: TeamFavoritePresenter
    private lateinit var adapter: TeamFavoriteAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        team_appbar.gone()
        team_spinner.gone()

        presenter = TeamFavoritePresenter(this, context)

        team_recycler.layoutManager = LinearLayoutManager(context)
        adapter = TeamFavoriteAdapter(teams) {
            context?.startActivity<DetailTeamActivity>("teamId" to it.teamId?.toInt())
        }

        team_recycler.adapter = adapter

        presenter.getData()

        team_swipe_refresh?.onRefresh {
            presenter.getData()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.getData()
    }

    override fun showLoading() {
        team_swipe_refresh?.let {
            if (!it.isRefreshing) {
                team_progressbar?.visible()
            }
        }
        team_message?.gone()
    }

    override fun hideLoading() {
        team_progressbar?.invisible()
        team_swipe_refresh?.let {
            if (it.isRefreshing) {
                it.isRefreshing = false
            }
        }
    }

    override fun dataNotFound(str: String?) {
        hideLoading()
        team_message?.let {
            it.text = if (str.isNullOrEmpty()) resources.getString(R.string.data_error) else str
            it.visible()
        }
    }

    override fun showData(result: List<TeamTable>) {
        val activity = activity
        if (activity != null && isAdded) {
            teams.clear()
            teams.addAll(result)
            adapter.notifyDataSetChanged()
            if (teams.size == 0) {
                dataNotFound(resources.getString(R.string.data_empty))
            }
        }
    }
}
