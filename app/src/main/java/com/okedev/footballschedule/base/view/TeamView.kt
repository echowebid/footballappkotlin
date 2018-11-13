package com.okedev.footballschedule.base.view

import com.okedev.footballschedule.base.callback.TeamCallback
import com.okedev.footballschedule.data.network.response.TeamResponse

interface TeamView : TeamCallback<TeamResponse>  {
    fun hideLoading()
    fun showLoading()
}