package com.bramgoedvriend.glaswerk.ui

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.bramgoedvriend.glaswerk.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ChangeRoomAndClass {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun changeRoomAndClass() {
        val constraintLayout = onView(
            allOf(
                withId(R.id.lokaal),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.header),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        constraintLayout.perform(click())

        val cardView = onView(
            allOf(
                withId(R.id.card_room),
                childAtPosition(
                    allOf(
                        withId(R.id.list),
                        childAtPosition(
                            withId(R.id.popup_bottom_overlay),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        cardView.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.lokaal_name), withText("1.015"),
                childAtPosition(
                    allOf(
                        withId(R.id.lokaal),
                        childAtPosition(
                            IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("1.015")))

        val constraintLayout2 = onView(
            allOf(
                withId(R.id.klas),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.header),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        constraintLayout2.perform(click())

        val cardView2 = onView(
            allOf(
                withId(R.id.card_class),
                childAtPosition(
                    allOf(
                        withId(R.id.list),
                        childAtPosition(
                            withId(R.id.popup_bottom_overlay),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        cardView2.perform(click())

        val textView2 = onView(
            allOf(
                withId(R.id.klas_name), withText("6B"),
                childAtPosition(
                    allOf(
                        withId(R.id.klas),
                        childAtPosition(
                            IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("6B")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>,
        position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent) &&
                        view == parent.getChildAt(position)
            }
        }
    }
}
