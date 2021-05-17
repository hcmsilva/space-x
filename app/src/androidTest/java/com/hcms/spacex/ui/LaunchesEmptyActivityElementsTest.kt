package com.hcms.spacex.ui


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.hcms.spacex.R
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LaunchesEmptyActivityElementsTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LaunchesActivity::class.java)

    @Test
    fun launchesEmptyActivityElementsTest() {
        val textView = onView(
            allOf(
                withId(R.id.title), withText("SpaceX"),
                withParent(withParent(withId(R.id.spacex_toolbar))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("SpaceX")))

        val imageButton = onView(
            allOf(
                withId(R.id.filter_button),
                withParent(withParent(withId(R.id.spacex_toolbar))),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val textView2 = onView(
            allOf(
                withId(R.id.company_info_divider), withText("COMPANY"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("COMPANY")))

        val textView3 = onView(
            allOf(
                withId(R.id.launches_divider), withText("LAUNCHES"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("LAUNCHES")))
    }
}
