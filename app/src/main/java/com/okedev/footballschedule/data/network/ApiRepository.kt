package com.okedev.footballschedule.data.network

import com.okedev.footballschedule.data.network.response.EventResponse
import com.okedev.footballschedule.data.network.response.PlayerResponse
import com.okedev.footballschedule.data.network.response.TeamResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRepository {
    @GET("eventsnextleague.php")
    fun getEventNextLeague(@Query("id") id: Int?): Call<EventResponse>

    @GET("eventspastleague.php")
    fun getEventPastleague(@Query("id") id: Int?): Call<EventResponse>

    @GET("lookupevent.php")
    fun getLookupEvent(@Query("id") id: Int?): Call<EventResponse>

    @GET("lookupplayer.php")
    fun getLookupPlayer(@Query("id") id: Int?): Call<PlayerResponse>

    @GET("lookup_all_players.php")
    fun getLookupAllPlayer(@Query("id") id: Int?): Call<PlayerResponse>

    @GET("lookupteam.php")
    fun getLookupTeam(@Query("id") id: Int?): Call<TeamResponse>

    @GET("lookup_all_teams.php")
    fun getLookupAllTeams(@Query("id") id: Int?): Call<TeamResponse>

    @GET("searchevents.php")
    fun getSearchEvent(@Query("e") e: String?): Call<EventResponse>

    @GET("searchteams.php")
    fun getSearchTeams(@Query("t") t: String?): Call<TeamResponse>
}