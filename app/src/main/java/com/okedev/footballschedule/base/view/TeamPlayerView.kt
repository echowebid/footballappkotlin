package com.okedev.footballschedule.base.view

import com.okedev.footballschedule.base.callback.TeamPlayerCallback
import com.okedev.footballschedule.data.network.response.PlayerResponse

interface TeamPlayerView : TeamPlayerCallback<PlayerResponse> {
    fun hideLoading()
    fun showLoading()
}