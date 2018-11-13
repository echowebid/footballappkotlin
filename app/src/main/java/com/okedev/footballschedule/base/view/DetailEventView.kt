package com.okedev.footballschedule.base.view

import com.okedev.footballschedule.base.callback.DetailEventCallback
import com.okedev.footballschedule.base.callback.AwayTeamCallback
import com.okedev.footballschedule.base.callback.HomeTeamCallback
import com.okedev.footballschedule.data.network.response.Event
import com.okedev.footballschedule.data.network.response.Team

interface DetailEventView : DetailEventCallback<Event>, HomeTeamCallback<Team>, AwayTeamCallback<Team> {
    fun hideLoading()
    fun showLoading()
    fun showSnackBar(m: String)
    fun setFavorite(b: Boolean)
}