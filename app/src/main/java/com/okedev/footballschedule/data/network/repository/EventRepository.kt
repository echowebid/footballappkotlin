package com.okedev.footballschedule.data.network.repository

import android.util.Log
import com.okedev.footballschedule.base.callback.*
import com.okedev.footballschedule.data.network.ApiRepository
import com.okedev.footballschedule.data.network.FootballService
import com.okedev.footballschedule.data.network.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository {

    private lateinit var callAwayTeam: Call<TeamResponse>
    private lateinit var callDetailTeam: Call<TeamResponse>
    private lateinit var callEvent: Call<EventResponse>
    private lateinit var callHomeTeam: Call<TeamResponse>
    private lateinit var callNextMatch: Call<EventResponse>
    private lateinit var callPasMatch: Call<EventResponse>
    private lateinit var callTeam: Call<TeamResponse>
    private lateinit var callPlayer: Call<PlayerResponse>
    private lateinit var callPlayerTeam: Call<PlayerResponse>
    private lateinit var callSearchEvent: Call<EventResponse>
    private lateinit var callSearchTeam: Call<TeamResponse>

    fun getEvent(event: Int?, callback: DetailEventCallback<Event>) {
        callEvent = FootballService
            .createService(ApiRepository::class.java)
            .getLookupEvent(event)

        callEvent.enqueue(object : Callback<EventResponse> {
            override fun onFailure(call: Call<EventResponse>?, t: Throwable?) {
                callback.dataNotFound(t.toString())
            }
            override fun onResponse(call: Call<EventResponse>?, response: Response<EventResponse>?) {
                response?.let { responseEvent ->
                    if (responseEvent.isSuccessful) {
                        responseEvent.body()?.events?.let {
                            callback.showEvent(it[0])
                        }
                    } else {
                        callback.dataNotFound("")
                    }
                }
            }
        })
    }

    fun getSearchEvent(e: String?, callback: EventCallback<EventResponse>) {
        callSearchEvent = FootballService
                .createService(ApiRepository::class.java)
                .getSearchEvent(e)

        callSearchEvent.enqueue(object : Callback<EventResponse> {
            override fun onFailure(call: Call<EventResponse>?, t: Throwable?) {
                callback.dataNotFound(t.toString())
            }
            override fun onResponse(call: Call<EventResponse>?, response: Response<EventResponse>?) {
                response?.let {
                    if (it.isSuccessful) {
                        callback.showData(it.body())
                    } else {
                        callback.dataNotFound("")
                    }
                }
            }
        })
    }

    fun getPastMatch(league: Int?, callback: EventCallback<EventResponse>) {
        callPasMatch = FootballService
            .createService(ApiRepository::class.java)
            .getEventPastleague(league)

        callPasMatch.enqueue(object : Callback<EventResponse> {
                override fun onFailure(call: Call<EventResponse>?, t: Throwable?) {
                    callback.dataNotFound(t.toString())
                }
                override fun onResponse(call: Call<EventResponse>?, response: Response<EventResponse>?) {
                    response?.let {
                        if (it.isSuccessful) {
                            callback.showData(it.body())
                        } else {
                            callback.dataNotFound("")
                        }
                    }
                }
            })
    }

    fun getNextMatch(league: Int?, callback: EventCallback<EventResponse>) {
        callNextMatch = FootballService
            .createService(ApiRepository::class.java)
            .getEventNextLeague(league)

        callNextMatch.enqueue(object : Callback<EventResponse> {
                override fun onFailure(call: Call<EventResponse>?, t: Throwable?) {
                    callback.dataNotFound(t.toString())
                }
                override fun onResponse(call: Call<EventResponse>?, response: Response<EventResponse>?) {
                    response?.let {
                        if (it.isSuccessful) {
                            callback.showData(it.body())
                        } else {
                            callback.dataNotFound("")
                        }
                    }
                }
            })
    }

    fun getTeam(leagueId: Int?, callback: TeamCallback<TeamResponse>) {
        callTeam = FootballService
                .createService(ApiRepository::class.java)
                .getLookupAllTeams(leagueId)

        callTeam.enqueue(object : Callback<TeamResponse> {
            override fun onFailure(call: Call<TeamResponse>?, t: Throwable?) {
                callback.dataNotFound(t.toString())
            }
            override fun onResponse(call: Call<TeamResponse>?, response: Response<TeamResponse>?) {
                response?.let {
                    if (it.isSuccessful) {
                        callback.showData(it.body())
                    } else {
                        callback.dataNotFound("")
                    }
                }
            }
        })
    }

    fun getSearchTeam(t: String?, callback: TeamCallback<TeamResponse>) {
        callSearchTeam = FootballService
                .createService(ApiRepository::class.java)
                .getSearchTeams(t)

        callSearchTeam.enqueue(object : Callback<TeamResponse> {
            override fun onFailure(call: Call<TeamResponse>?, t: Throwable?) {
                callback.dataNotFound(t.toString())
            }
            override fun onResponse(call: Call<TeamResponse>?, response: Response<TeamResponse>?) {
                response?.let {
                    if (it.isSuccessful) {
                        callback.showData(it.body())
                    } else {
                        callback.dataNotFound("")
                    }
                }
            }
        })
    }

    fun getAwayTeam(team: Int?, callback: AwayTeamCallback<Team>) {
        callAwayTeam = FootballService
            .createService(ApiRepository::class.java)
            .getLookupTeam(team)

        callAwayTeam.enqueue(object : Callback<TeamResponse> {
                override fun onFailure(call: Call<TeamResponse>?, t: Throwable?) {
                }
                override fun onResponse(call: Call<TeamResponse>?, response: Response<TeamResponse>?) {
                    response?.let { responseAwayTeam ->
                        if (responseAwayTeam.isSuccessful) {
                            responseAwayTeam.body()?.teams?.let {
                                callback.showAwayTeam(it[0])
                            }
                        }
                    }
                }
            })
    }

    fun getHomeTeam(team: Int?, callback: HomeTeamCallback<Team>) {
        callHomeTeam = FootballService
                .createService(ApiRepository::class.java)
                .getLookupTeam(team)

        callHomeTeam.enqueue(object : Callback<TeamResponse> {
            override fun onFailure(call: Call<TeamResponse>?, t: Throwable?) {
                //data not found
            }
            override fun onResponse(call: Call<TeamResponse>?, response: Response<TeamResponse>?) {
                response?.let { responseHomeTeam ->
                    if (responseHomeTeam.isSuccessful) {
                        responseHomeTeam.body()?.teams?.let {
                            callback.showHomeTeam(it[0])
                        }
                    }
                }
            }
        })
    }

    fun getDetailTeam(team: Int?, callback: DetailTeamCallback<Team>) {
        callDetailTeam = FootballService
                .createService(ApiRepository::class.java)
                .getLookupTeam(team)

        callDetailTeam.enqueue(object : Callback<TeamResponse> {
            override fun onFailure(call: Call<TeamResponse>?, t: Throwable?) {
            }
            override fun onResponse(call: Call<TeamResponse>?, response: Response<TeamResponse>?) {
                response?.let { responseAwayTeam ->
                    if (responseAwayTeam.isSuccessful) {
                        responseAwayTeam.body()?.teams?.let {
                            callback.showData(it[0])
                        }
                    }
                }
            }
        })
    }

    fun getPlayer(playerId: Int?, callback: DetailPlayerCallback<Player>) {
        callPlayer = FootballService
                .createService(ApiRepository::class.java)
                .getLookupPlayer(playerId)

        callPlayer.enqueue(object : Callback<PlayerResponse> {
            override fun onFailure(call: Call<PlayerResponse>?, t: Throwable?) {
                callback.dataNotFound(t.toString())
            }
            override fun onResponse(call: Call<PlayerResponse>?, response: Response<PlayerResponse>?) {
                response?.let { responseEvent ->
                    if (responseEvent.isSuccessful) {
                        responseEvent.body()?.players?.let {
                            callback.showData(it[0])
                        }
                    } else {
                        callback.dataNotFound("")
                    }
                }
            }
        })
    }

    fun getPlayerTeam(teamId: Int?, callback: TeamPlayerCallback<PlayerResponse>) {
        callPlayerTeam = FootballService
                .createService(ApiRepository::class.java)
                .getLookupAllPlayer(teamId)

        callPlayerTeam.enqueue(object : Callback<PlayerResponse> {
            override fun onFailure(call: Call<PlayerResponse>?, t: Throwable?) {
                callback.dataNotFound(t.toString())
            }
            override fun onResponse(call: Call<PlayerResponse>?, response: Response<PlayerResponse>?) {
                response?.let {
                    if (it.isSuccessful) {
                        callback.showData(it.body())
                    }
                }
            }
        })
    }

    fun detachProcess()
    {
        if (this::callEvent.isInitialized)
            callEvent.cancel()

        if (this::callPasMatch.isInitialized)
            callPasMatch.cancel()

        if (this::callNextMatch.isInitialized)
            callNextMatch.cancel()

        if (this::callTeam.isInitialized)
            callTeam.cancel()

        if (this::callDetailTeam.isInitialized)
            callDetailTeam.cancel()

        if (this::callAwayTeam.isInitialized)
            callAwayTeam.cancel()

        if (this::callHomeTeam.isInitialized)
            callHomeTeam.cancel()

        if (this::callPlayer.isInitialized)
            callPlayer.cancel()

        if (this::callPlayerTeam.isInitialized)
            callPlayerTeam.cancel()

        if (this::callSearchEvent.isInitialized)
            callSearchEvent.cancel()

        if (this::callSearchTeam.isInitialized)
            callSearchTeam.cancel()
    }

}