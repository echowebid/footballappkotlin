package com.okedev.footballschedule.base.activity

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.okedev.footballschedule.R
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testRecyclerViewBehaviour() {
        delayProcess()

        val recyclerView = onView(CoreMatchers.allOf(isDisplayed(), withId(R.id.event_recycler)))
        recyclerView.check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun testBottomNavigationBehaviour() {

        delayProcess()

        onView(withId(R.id.menu_team)).perform(click())

        delayProcess()

        onView(withId(R.id.menu_favorite)).perform(click())

        delayProcess()

        onView(withId(R.id.menu_match)).perform(click())

        delayProcess()
    }

    @Test
    fun testAppBehaviour() {

        delayProcess()

        onView(withId(R.id.main_event_spinner)).perform(click())
        onView(withText("Italian Serie A")).perform(click())

        delayProcess()

        onView(withText("Inter")).perform(click())

        delayProcess()

        onView(withId(R.id.add_to_favorite)).perform(click())

        delayProcess()

        Espresso.pressBack()

        delayProcess()

        onView(withId(R.id.menu_favorite)).perform(click())

        delayProcess()

        onView(withId(R.id.menu_team)).perform(click())

        delayProcess()

        onView(withText("Atalanta")).perform(click())

        delayProcess()

    }

    private fun delayProcess(m: Long = 2000) {
        try {
            Thread.sleep(m)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}