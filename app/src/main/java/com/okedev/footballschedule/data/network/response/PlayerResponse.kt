package com.okedev.footballschedule.data.network.response

import com.google.gson.annotations.SerializedName

data class PlayerResponse (
    @SerializedName("player")
    var player: ArrayList<Player>? = null,

    @SerializedName("players")
    var players: ArrayList<Player>? = null
)