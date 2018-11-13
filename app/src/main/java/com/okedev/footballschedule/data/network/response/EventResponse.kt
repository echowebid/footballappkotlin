package com.okedev.footballschedule.data.network.response

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("events")
    var events: ArrayList<Event>? = null,

    @SerializedName("event")
    var event: ArrayList<Event>? = null
)