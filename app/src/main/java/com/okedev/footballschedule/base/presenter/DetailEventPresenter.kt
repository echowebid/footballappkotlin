package com.okedev.footballschedule.base.presenter

import android.database.sqlite.SQLiteConstraintException
import com.okedev.footballschedule.base.callback.DetailEventCallback
import com.okedev.footballschedule.base.callback.AwayTeamCallback
import com.okedev.footballschedule.base.callback.HomeTeamCallback
import com.okedev.footballschedule.base.view.DetailEventView
import com.okedev.footballschedule.data.db.MyDBHelper
import com.okedev.footballschedule.data.db.model.EventTable
import com.okedev.footballschedule.data.network.repository.EventRepository
import com.okedev.footballschedule.data.network.response.Event
import com.okedev.footballschedule.data.network.response.Team
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class DetailEventPresenter(private val view: DetailEventView,
                           private val eventRepository: EventRepository) {

    fun getAwayTeam(team: Int?) {
        eventRepository.getAwayTeam(team, object: AwayTeamCallback<Team>{
            override fun showAwayTeam(team: Team) {
                view.showAwayTeam(team)
                view.hideLoading()
            }
        })
    }

    fun getHomeTeam(team: Int?) {
        eventRepository.getHomeTeam(team, object: HomeTeamCallback<Team>{
            override fun showHomeTeam(team: Team) {
                view.showHomeTeam(team)
                view.hideLoading()
            }
        })
    }

    fun getEvent(event: Int?) {
        view.showLoading()
        eventRepository.getEvent(event, object: DetailEventCallback<Event>{
            override fun dataNotFound(str: String?) {
                view.dataNotFound(str)
                view.hideLoading()
            }
            override fun showEvent(data: Event) {
                view.showEvent(data)
                view.hideLoading()
            }
        })
    }

    fun addToFavorite(database: MyDBHelper, event: Event){
        try {
            database.use {
                insert(EventTable.EVENT_FAVORITE,
                        EventTable.EVENT_ID to event.idEvent,
                        EventTable.EVENT_NAME to event.strEvent,
                        EventTable.EVENT_DATE to event.dateEvent,
                        EventTable.EVENT_TIME to event.strTime,
                        EventTable.HOME_TEAM_ID to event.idHomeTeam,
                        EventTable.HOME_TEAM_NAME to event.strHomeTeam,
                        EventTable.HOME_TEAM_SCORE to event.intHomeScore,
                        EventTable.AWAY_TEAM_ID to event.idAwayTeam,
                        EventTable.AWAY_TEAM_NAME to event.strAwayTeam,
                        EventTable.AWAY_TEAM_SCORE to event.intAwayScore,
                        EventTable.LEAGUE_ID to event.idLeague,
                        EventTable.LEAGUE_NAME to event.strLeague)
            }
            view.showSnackBar("Added to favorite")
            view.setFavorite(true)
        } catch (e: SQLiteConstraintException){
            view.showSnackBar(e.localizedMessage)
        }
    }

    fun removeFromFavorite(database: MyDBHelper, idEvent: String) {
        try {
            database.use {
                delete(
                        EventTable.EVENT_FAVORITE,
                        "EVENT_ID = {event_id}",
                        "event_id" to idEvent
                )
            }
            view.showSnackBar("Removed to favorite")
            view.setFavorite(false)
        } catch (e: SQLiteConstraintException) {
            view.showSnackBar(e.localizedMessage)
        }
    }

    fun favoriteEvent(database: MyDBHelper, idEvent: String){
        database.use {
            val result = select(EventTable.EVENT_FAVORITE)
                    .whereArgs(
                            "(EVENT_ID = {event_id})",
                            "event_id" to idEvent
                    )
            val favorite = result.parseList(classParser<EventTable>())
            val isFavorite = !favorite.isEmpty()
            view.setFavorite(isFavorite)
        }
    }

    fun detachProcess()
    {
        eventRepository.detachProcess()
    }
}