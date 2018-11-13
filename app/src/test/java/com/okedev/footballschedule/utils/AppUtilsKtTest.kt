package com.okedev.footballschedule.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat

class AppUtilsKtTest {

    @Test
    fun testStrToDate() {
        val expect = SimpleDateFormat("yyyy-MM-dd").parse("2018-09-23")
        assertEquals(expect, strToDate("2018-09-23", "yyyy-MM-dd"))
    }

    @Test
    fun testFormatDate() {
        val except = "Sun, 23 Sep 2018"
        assertEquals(except, formatDate(strToDate("2018-09-23")))
    }
}