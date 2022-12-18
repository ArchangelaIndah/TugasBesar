package com.example.tubes


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class PemesananActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(PemesananActivity::class.java)

    @Test
    fun pemesananActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(500)

        val materialButton = onView(
            allOf(withId(R.id.button_create), withText("Tambah Pemesanan"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    2),
                isDisplayed()))
        materialButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(700)

        val appCompatEditText = onView(
            allOf(withId(R.id.edit_nama),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    1),
                isDisplayed()))
        appCompatEditText.perform(click())

        val appCompatEditText2 = onView(
            allOf(withId(R.id.edit_nama),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    1),
                isDisplayed()))
        appCompatEditText2.perform(replaceText("oli"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(withId(R.id.edit_jumlah),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    2),
                isDisplayed()))
        appCompatEditText3.perform(replaceText("1"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(withId(R.id.edit_alamatBengkel),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    3),
                isDisplayed()))
        appCompatEditText4.perform(replaceText("babarsari"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(withId(R.id.button_save), withText("SAVE"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    4),
                isDisplayed()))
        materialButton2.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int,
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
