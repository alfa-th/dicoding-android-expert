package com.dicoding.picodicloma.myunitanduitesting

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    private val dummyL = "12.0"
    private val dummyW = "7.0"
    private val dummyH = "6.0"

    private val dummyVol = "504.0"
    private val dummyCir = "100.0"
    private val dummySur = "396.0"

    private val emptyInput = ""
    private val fieldEmpty = "Field ini tidak boleh kosong"

    @get:Rule
    var mActivityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun assertGetCircumference() {
        onView(withId(R.id.edt_length)).perform(typeText(dummyL), closeSoftKeyboard())
        onView(withId(R.id.edt_width)).perform(typeText(dummyW), closeSoftKeyboard())
        onView(withId(R.id.edt_height)).perform(typeText(dummyH), closeSoftKeyboard())

        onView(withId(R.id.btn_save)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_save)).perform(click())

        onView(withId(R.id.btn_calculate_circumference)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_calculate_circumference)).perform(click())

        onView(withId(R.id.tv_result)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_result)).check(matches(withText(dummyCir)))
    }

    @Test
    fun assertGetVolume() {
        onView(withId(R.id.edt_length)).perform(typeText(dummyL), closeSoftKeyboard())
        onView(withId(R.id.edt_width)).perform(typeText(dummyW), closeSoftKeyboard())
        onView(withId(R.id.edt_height)).perform(typeText(dummyH), closeSoftKeyboard())

        onView(withId(R.id.btn_save)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_save)).perform(click())

        onView(withId(R.id.btn_calculate_volume)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_calculate_volume)).perform(click())

        onView(withId(R.id.tv_result)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_result)).check(matches(withText(dummyVol)))
    }

    @Test
    fun assertGetSurfaceArea() {
        onView(withId(R.id.edt_length)).perform(typeText(dummyL), closeSoftKeyboard())
        onView(withId(R.id.edt_width)).perform(typeText(dummyW), closeSoftKeyboard())
        onView(withId(R.id.edt_height)).perform(typeText(dummyH), closeSoftKeyboard())

        onView(withId(R.id.btn_save)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_save)).perform(click())

        onView(withId(R.id.btn_calculate_surface_area)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_calculate_surface_area)).perform(click())

        onView(withId(R.id.tv_result)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_result)).check(matches(withText(dummySur)))
    }

    @Test
    fun assertEmptyInput() {
        onView(withId(R.id.edt_length))
            .perform(typeText(emptyInput))
            .perform(closeSoftKeyboard())

        onView(withId(R.id.btn_save))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.edt_length))
            .check(matches(hasErrorText(fieldEmpty)))
            .perform(typeText(dummyL))
            .perform(closeSoftKeyboard())

        //

        onView(withId(R.id.edt_width))
            .perform(typeText(emptyInput))
            .perform(closeSoftKeyboard())

        onView(withId(R.id.btn_save))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.edt_width))
            .check(matches(hasErrorText(fieldEmpty)))
            .perform(typeText(dummyW))
            .perform(closeSoftKeyboard())

        //

        onView(withId(R.id.edt_height))
            .perform(typeText(emptyInput))
            .perform(closeSoftKeyboard())

        onView(withId(R.id.btn_save))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.edt_height))
            .check(matches(hasErrorText(fieldEmpty)))
            .perform(typeText(dummyH))
            .perform(closeSoftKeyboard())
    }
}