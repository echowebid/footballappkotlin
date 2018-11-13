package com.okedev.footballschedule.base.presenter

import com.okedev.footballschedule.base.callback.EventCallback
import com.okedev.footballschedule.base.view.EventView
import com.okedev.footballschedule.data.network.repository.EventRepository
import com.okedev.footballschedule.data.network.response.EventResponse

class EventPresenter (private val view: EventView,
                      private val eventRepository: EventRepository) {

    fun getData(league: Int?, target: Int?) {
        view.showLoading()
        if (target == 1 ) {
            eventRepository.getPastMatch(league, object: EventCallback<EventResponse> {
                override fun showData(data: EventResponse?) {
                    view.showData(data)
                }
                override fun dataNotFound(str: String?) {
                    view.dataNotFound(str)
                }
            })
        } else if (target == 2) {
            eventRepository.getNextMatch(league, object: EventCallback<EventResponse> {
                override fun showData(data: EventResponse?) {
                    view.showData(data)
                }
                override fun dataNotFound(str: String?) {
                    view.dataNotFound(str)
                }
            })
        }
    }

    fun searchData(e: String?) {
        view.showLoading()
        eventRepository.getSearchEvent(e, object: EventCallback<EventResponse> {
            override fun showData(data: EventResponse?) {
                view.showData(data)
            }
            override fun dataNotFound(str: String?) {
                view.dataNotFound(str)
            }
        })
    }

    fun detachProcess()
    {
        eventRepository.detachProcess()
    }
}