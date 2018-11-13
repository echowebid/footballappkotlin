package com.okedev.footballschedule.base.presenter

import com.okedev.footballschedule.base.callback.TeamCallback
import com.okedev.footballschedule.base.view.TeamView
import com.okedev.footballschedule.data.network.repository.EventRepository
import com.okedev.footballschedule.data.network.response.TeamResponse

class TeamPresenter (private val view: TeamView, private val eventRepository: EventRepository) {
    fun getData(leagueId: Int?) {
        view.showLoading()
        eventRepository.getTeam(leagueId, object: TeamCallback<TeamResponse>{
            override fun showData(data: TeamResponse?) {
                view.showData(data)
            }

            override fun dataNotFound(str: String?) {
                view.dataNotFound(str)
            }
        })
    }

    fun searchData(t: String?) {
        view.showLoading()
        eventRepository.getSearchTeam(t, object: TeamCallback<TeamResponse>{
            override fun showData(data: TeamResponse?) {
                view.showData(data)
            }

            override fun dataNotFound(str: String?) {
                view.dataNotFound(str)
            }
        })
    }

    fun detachProcess() {
        eventRepository.detachProcess()
    }
}