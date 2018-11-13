package com.okedev.footballschedule.base.view

import com.okedev.footballschedule.data.db.model.PlayerTable

interface PlayerFavoriteView {
    fun dataNotFound(str: String?)
    fun hideLoading()
    fun showData(result: List<PlayerTable>)
    fun showLoading()
}