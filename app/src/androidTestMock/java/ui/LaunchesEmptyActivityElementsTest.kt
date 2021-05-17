package com.hcms.spacex.ui


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.hcms.spacex.R
import com.hcms.spacex.ui.utils.CountingIdlingResourceSingleton
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class LaunchesEmptyActivityElementsTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<LaunchesActivity> =
        ActivityScenarioRule(LaunchesActivity::class.java)

    //todo this one doesn't have network reqs
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance()
            .register(CountingIdlingResourceSingleton.countingIdlingResCompanyInfo)
        IdlingRegistry.getInstance()
            .register(CountingIdlingResourceSingleton.countingIdlingResAllLaunches)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance()
            .unregister(CountingIdlingResourceSingleton.countingIdlingResCompanyInfo)
        IdlingRegistry.getInstance()
            .unregister(CountingIdlingResourceSingleton.countingIdlingResAllLaunches)
    }

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
