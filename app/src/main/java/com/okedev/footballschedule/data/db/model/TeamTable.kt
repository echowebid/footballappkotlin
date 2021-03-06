package com.okedev.footballschedule.data.db.model

class TeamTable(
        val id: Long?,
        val teamId: String?,
        val teamName: String?,
        val teamBadge: String?
) {
    companion object {
        const val TEAM_FAVORITE: String = "TEAM_FAVORITE"
        const val ID: String = "ID_"
        const val TEAM_ID: String = "TEAM_ID"
        const val TEAM_NAME: String = "TEAM_NAME"
        const val TEAM_BADGE: String = "TEAM_BADGE"
    }
}