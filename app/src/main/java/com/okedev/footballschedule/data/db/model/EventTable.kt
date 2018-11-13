package com.okedev.footballschedule.data.db.model

class EventTable(
        val id: Long?,
        val eventId: String?,
        val eventName: String?,
        val eventDate: String?,
        val eventTime: String?,
        val homeTeamId: String?,
        val homeTeamName: String?,
        val homeTeamScore: String?,
        val awayTeamId: String?,
        val awayTeamName: String?,
        val awayTeamScore: String?,
        val leagueId: String?,
        val leagueName: String?
) {
    companion object {
        const val EVENT_FAVORITE: String = "EVENT_FAVORITE"
        const val ID: String = "ID_"
        const val EVENT_ID: String = "EVENT_ID"
        const val EVENT_NAME: String = "EVENT_NAME"
        const val EVENT_DATE: String = "EVENT_DATE"
        const val EVENT_TIME: String = "EVENT_TIME"
        const val HOME_TEAM_ID: String = "HOME_TEAM_ID"
        const val HOME_TEAM_NAME: String = "HOME_TEAM_NAME"
        const val HOME_TEAM_SCORE: String = "HOME_TEAM_SCORE"
        const val AWAY_TEAM_ID: String = "AWAY_TEAM_ID"
        const val AWAY_TEAM_NAME: String = "AWAY_TEAM_NAME"
        const val AWAY_TEAM_SCORE: String = "AWAY_TEAM_SCORE"
        const val LEAGUE_ID: String = "LEAGUE_ID"
        const val LEAGUE_NAME: String = "LEAGUE_NAME"
    }
}