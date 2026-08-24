package com.example.obd2diagnostic

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testDashboardElementsVisible() {
        // Check Toolbar title
        onView(withText("Car Scanner Pro")).check(matches(isDisplayed()))

        // Check ECU ID header
        onView(withText("ECU ID: 7E8")).check(matches(isDisplayed()))

        // Check a few menu items
        onView(withText("Dashboard")).check(matches(isDisplayed()))
        onView(withText("Live data")).check(matches(isDisplayed()))
        onView(withText("Terminal")).check(matches(isDisplayed()))

        // Check Disconnect button
        onView(withId(R.id.btn_disconnect)).check(matches(isDisplayed()))
        onView(withText("DISCONNECT")).check(matches(isDisplayed()))
    }
}
