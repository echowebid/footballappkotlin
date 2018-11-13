package com.okedev.footballschedule.base.view

import com.okedev.footballschedule.base.callback.EventCallback
import com.okedev.footballschedule.data.network.response.EventResponse

interface EventView : EventCallback<EventResponse> {
    fun hideLoading()
    fun showLoading()
}