package com.okedev.footballschedule.base.view

import com.okedev.footballschedule.data.db.model.EventTable

interface EventFavoriteView {
    fun dataNotFound(str: String?)
    fun hideLoading()
    fun showData(result: List<EventTable>)
    fun showLoading()
}