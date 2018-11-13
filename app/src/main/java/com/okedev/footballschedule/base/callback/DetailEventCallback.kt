package com.okedev.footballschedule.base.callback

interface DetailEventCallback<T> {
    fun dataNotFound(str: String?)
    fun showEvent(data: T)
}