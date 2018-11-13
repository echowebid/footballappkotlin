package com.okedev.footballschedule.data.network.response

import com.google.gson.annotations.SerializedName

data class TeamResponse (
    @SerializedName("teams")
    var teams: ArrayList<Team>? = null
)