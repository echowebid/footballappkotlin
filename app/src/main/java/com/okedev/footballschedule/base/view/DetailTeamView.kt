package com.okedev.footballschedule.base.view

import com.okedev.footballschedule.base.callback.DetailTeamCallback
import com.okedev.footballschedule.data.network.response.Team

interface DetailTeamView : DetailTeamCallback<Team> {
    fun showLoading()
    fun hideLoading()
    fun showSnackBar(m: String)
    fun setFavorite(b: Boolean)
}