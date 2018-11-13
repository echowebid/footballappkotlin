package com.okedev.footballschedule.base.callback

interface EventCallback<T> {
    fun showData(data: T?)
    fun dataNotFound(str: String?)
}