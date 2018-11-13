package com.okedev.footballschedule.base.view

import com.okedev.footballschedule.data.db.model.TeamTable

interface TeamFavoriteView {
    fun dataNotFound(str: String?)
    fun hideLoading()
    fun showData(result: List<TeamTable>)
    fun showLoading()
}