package com.okedev.footballschedule.utils

import android.annotation.SuppressLint
import android.view.View
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("SimpleDateFormat")
fun strToDate(str: String?, sdf: String = "yyyy-MM-dd"): Date {
    val simpleDateFormat = SimpleDateFormat(sdf)
    simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return simpleDateFormat.parse(str)
}

fun strToDateTime(str: String?): Date {
    return strToDate(str, "yyyy-MM-dd HH:mm:ss")
}

fun strToTime(str: String?): Date {
    return strToDate(str, "HH:mm:ss")
}

@SuppressLint("SimpleDateFormat")
fun formatDate(date: Date?, sdf: String = "EEE, dd MMM yyyy"): String{
    val simpleDateFormat = SimpleDateFormat(sdf)
    return simpleDateFormat.format(date)
}

fun formatTime(t: Date?): String{
    return formatDate(t, "HH:mm") + " WIB"
}

@SuppressLint("SimpleDateFormat")
fun dateToStr(date: Date?): String {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return simpleDateFormat.format(date)
}

fun minuteToMilliSecond(m: Long): Long {
    return TimeUnit.MINUTES.toMillis(m)
}

fun strToMilliSecond(str: String?): Long{
    return strToDateTime(str).time
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}