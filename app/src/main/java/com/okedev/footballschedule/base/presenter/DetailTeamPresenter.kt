package com.okedev.footballschedule.base.presenter

import android.database.sqlite.SQLiteConstraintException
import com.okedev.footballschedule.base.callback.DetailTeamCallback
import com.okedev.footballschedule.base.view.DetailTeamView
import com.okedev.footballschedule.data.db.MyDBHelper
import com.okedev.footballschedule.data.db.model.TeamTable
import com.okedev.footballschedule.data.network.repository.EventRepository
import com.okedev.footballschedule.data.network.response.Team
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class DetailTeamPresenter(private val view: DetailTeamView,
                          private val eventRepository: EventRepository) {

    fun getTeam(team: Int?) {
        view.showLoading()
        eventRepository.getDetailTeam(team, object: DetailTeamCallback<Team> {
            override fun showData(data: Team) {
                view.showData(data)
            }

            override fun dataNotFound(str: String?) {
                view.dataNotFound(str)
            }
        })
    }

    fun addToFavorite(database: MyDBHelper, team: Team){
        try {
            database.use {
                insert(TeamTable.TEAM_FAVORITE,
                        TeamTable.TEAM_ID to team.idTeam,
                        TeamTable.TEAM_NAME to team.strTeam,
                        TeamTable.TEAM_BADGE to team.strTeamBadge)
            }
            view.showSnackBar("Added to favorite")
            view.setFavorite(true)
        } catch (e: SQLiteConstraintException){
            view.showSnackBar(e.localizedMessage)
        }
    }

    fun removeFromFavorite(database: MyDBHelper, idTeam: String) {
        try {
            database.use {
                delete(
                        TeamTable.TEAM_FAVORITE,
                        "TEAM_ID = {team_id}",
                        "team_id" to idTeam
                )
            }
            view.showSnackBar("Removed to favorite")
            view.setFavorite(false)
        } catch (e: SQLiteConstraintException) {
            view.showSnackBar(e.localizedMessage)
        }
    }

    fun favoriteTeam(database: MyDBHelper, idTeam: String){
        database.use {
            val result = select(TeamTable.TEAM_FAVORITE)
                    .whereArgs(
                            "(TEAM_ID = {team_id})",
                            "team_id" to idTeam
                    )
            val favorite = result.parseList(classParser<TeamTable>())
            val isFavorite = !favorite.isEmpty()
            view.setFavorite(isFavorite)
        }
    }

    fun detachProcess() {
        eventRepository.detachProcess()
    }
}