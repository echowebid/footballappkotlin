package com.okedev.footballschedule.base.presenter

import android.database.sqlite.SQLiteConstraintException
import com.okedev.footballschedule.base.callback.DetailPlayerCallback
import com.okedev.footballschedule.base.view.DetailPlayerView
import com.okedev.footballschedule.data.db.MyDBHelper
import com.okedev.footballschedule.data.db.model.PlayerTable
import com.okedev.footballschedule.data.network.repository.EventRepository
import com.okedev.footballschedule.data.network.response.Player
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class DetailPlayerPresenter (private val view: DetailPlayerView,
                             private val eventRepository: EventRepository) {
    fun getData(playerId: Int?) {
        view.showLoading()
        eventRepository.getPlayer(playerId, object: DetailPlayerCallback<Player> {
            override fun showData(data: Player) {
                view.showData(data)
            }
            override fun dataNotFound(str: String?) {
                view.dataNotFound(str)
            }
        })
    }

    fun addToFavorite(database: MyDBHelper, player: Player){
        try {
            database.use {
                insert(PlayerTable.PLAYER_FAVORITE,
                        PlayerTable.PLAYER_ID to player.idPlayer,
                        PlayerTable.PLAYER_NAME to player.strPlayer,
                        PlayerTable.PLAYER_POSITION to player.strPosition,
                        PlayerTable.PLAYER_BANNER to player.strCutout)
            }
            view.showSnackBar("Added to favorite")
            view.setFavorite(true)
        } catch (e: SQLiteConstraintException){
            view.showSnackBar(e.localizedMessage)
        }
    }

    fun removeFromFavorite(database: MyDBHelper, idPlayer: String) {
        try {
            database.use {
                delete(
                        PlayerTable.PLAYER_FAVORITE,
                        "PLAYER_ID = {player_id}",
                        "player_id" to idPlayer
                )
            }
            view.showSnackBar("Removed to favorite")
            view.setFavorite(false)
        } catch (e: SQLiteConstraintException) {
            view.showSnackBar(e.localizedMessage)
        }
    }

    fun favoritePlayer(database: MyDBHelper, idPlayer: String){
        database.use {
            val result = select(PlayerTable.PLAYER_FAVORITE)
                    .whereArgs(
                            "(PLAYER_ID = {player_id})",
                            "player_id" to idPlayer
                    )
            val favorite = result.parseList(classParser<PlayerTable>())
            val isFavorite = !favorite.isEmpty()
            view.setFavorite(isFavorite)
        }
    }

    fun detachProcess()
    {
        eventRepository.detachProcess()
    }
}