package com.okedev.footballschedule.base.callback

interface DetailTeamCallback<T> {
    fun showData(data: T)
    fun dataNotFound(str: String?)
}