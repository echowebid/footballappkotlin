package com.okedev.footballschedule.base.view

import com.okedev.footballschedule.base.callback.DetailPlayerCallback
import com.okedev.footballschedule.data.network.response.Player

interface DetailPlayerView : DetailPlayerCallback<Player> {
    fun hideLoading()
    fun showLoading()
    fun showSnackBar(m: String)
    fun setFavorite(b: Boolean)
}