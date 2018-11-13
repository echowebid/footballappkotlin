package com.okedev.footballschedule.base.presenter

import com.okedev.footballschedule.base.callback.TeamPlayerCallback
import com.okedev.footballschedule.base.view.TeamPlayerView
import com.okedev.footballschedule.data.network.repository.EventRepository
import com.okedev.footballschedule.data.network.response.PlayerResponse

class TeamPlayerPresenter (private val view: TeamPlayerView, private val eventRepository: EventRepository) {
    fun getData(teamId: Int?) {
        view.showLoading()
        eventRepository.getPlayerTeam(teamId, object: TeamPlayerCallback<PlayerResponse> {
            override fun dataNotFound(str: String?) {
                view.dataNotFound(str)
            }

            override fun showData(data: PlayerResponse?) {
                view.showData(data)
            }
        })
    }

    fun detachProcess() {
        eventRepository.detachProcess()
    }
}