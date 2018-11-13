package com.okedev.footballschedule.base.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.okedev.footballschedule.R
import com.okedev.footballschedule.data.TeamData
import com.okedev.footballschedule.data.network.response.Team
import kotlinx.android.synthetic.main.fragment_team_overview.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class TeamOverviewFragment : Fragment() {

    private lateinit var team: Team

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_team_overview, container, false)
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }
    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onEvent(teamData: TeamData) {
        team = teamData.data
        team_overview?.text = team.strDescriptionEN
    }
}
