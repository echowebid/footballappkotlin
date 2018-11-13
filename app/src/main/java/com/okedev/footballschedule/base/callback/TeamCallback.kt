package com.okedev.footballschedule.base.callback

interface TeamCallback<T> {
    fun showData(data: T?)
    fun dataNotFound(str: String?)
}