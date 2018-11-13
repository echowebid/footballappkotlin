package com.okedev.footballschedule.data.db.model

class PlayerTable (
        val id: Long?,
        val playerId: String?,
        val playerName: String?,
        val playerPosition: String?,
        val playerBanner: String?
) {
    companion object {
        const val PLAYER_FAVORITE: String = "PLAYER_FAVORITE"
        const val ID: String = "ID_"
        const val PLAYER_ID: String = "PLAYER_ID"
        const val PLAYER_NAME: String = "PLAYER_NAME"
        const val PLAYER_POSITION: String = "PLAYER_POSITION"
        const val PLAYER_BANNER: String = "PLAYER_BANNER"
    }
}