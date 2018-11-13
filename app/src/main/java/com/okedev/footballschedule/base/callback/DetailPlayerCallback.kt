package com.okedev.footballschedule.base.callback

interface DetailPlayerCallback<T> {
    fun dataNotFound(str: String?)
    fun showData(data: T)
}