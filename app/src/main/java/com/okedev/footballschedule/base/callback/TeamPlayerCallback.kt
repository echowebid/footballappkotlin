package com.okedev.footballschedule.base.callback

interface TeamPlayerCallback<T> {
    fun showData(data: T?)
    fun dataNotFound(str: String?)
}