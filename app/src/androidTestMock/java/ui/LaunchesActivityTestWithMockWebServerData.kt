package com.hcms.spacex.ui


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.hcms.spacex.repo.MockFileReader
import com.hcms.spacex.ui.utils.CountingIdlingResourceSingleton
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import utils.*


@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class LaunchesActivityTestWithMockWebServerData {

    @get:Rule
    var activityRule: ActivityScenarioRule<LaunchesActivity> =
        ActivityScenarioRule(LaunchesActivity::class.java)


    companion object {
        lateinit var localWebServer: MockWebServer

        @JvmStatic
        @BeforeClass
        fun serverSetup() {
            //mock server dispatcher setup for this feature
            val dispatcher: Dispatcher = object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse {
                    val companyInfoRecordedResponse =
                        MockFileReader("/company_info_mock_response.json").content
                    val allLaunchesRecordedResponse =
                        MockFileReader("/all_launches_mock_response.json").content
                    when (request.path) {
                        "/v3/info" -> return MockResponse().setBody(companyInfoRecordedResponse)
                            .setResponseCode(200)
                        "/v3/launches" -> return MockResponse().setBody(allLaunchesRecordedResponse)
                            .setResponseCode(200)

                    }
                    return MockResponse().setResponseCode(404)
                }
            }
            IdlingRegistry.getInstance()
                .register(CountingIdlingResourceSingleton.countingIdlingResCompanyInfo)
            IdlingRegistry.getInstance()
                .register(CountingIdlingResourceSingleton.countingIdlingResAllLaunches)

            localWebServer = MockWebServer()
            localWebServer.dispatcher = dispatcher
            localWebServer.start(8080)
        }

        @JvmStatic
        @AfterClass
        fun serverTeardown() {
            IdlingRegistry.getInstance()
                .unregister(CountingIdlingResourceSingleton.countingIdlingResCompanyInfo)
            IdlingRegistry.getInstance()
                .unregister(CountingIdlingResourceSingleton.countingIdlingResAllLaunches)

            localWebServer.shutdown()
        }
    }

    private val empty = ""


    @Test
    fun givenUserStartsActivity_AndDataLoadsSuccessfully_WhenDataLoads_ThenCompleteViewHierarchyIsChecked() {
        //full hierarchy to check for the recorded json responses:

        //spaceXToolbar
        onView(spaceXMatcher())
            .check(matches(isDisplayed()))

        //spaceXToolbarTitle
        onView(spaceXTitleMatcher())
            .check(matches(withText("SpaceX")))

        //company header
        onView(companyDividerMatcher())
            .check(matches(isDisplayed()))

        //companyInfoTextView
        onView(companyInfoTextViewMatcher())
            .check(matches(withText("SpaceX was founded by Elon Musk in 2002. It has now 7000 employees, 3 launch sites, and is valued at USD 27500000000")))

        //filter button
        onView(filterButtonMatcher())
            .check(matches(hasContentDescription()))
            .check(matches(isDisplayed()))

        //launches divider
        onView(launchesDividerMatcher())
            .check(matches(isDisplayed()))


        //content list

        //missionTextView
        recyclerItemViewMatcher(0, missionLabelMatcher())
        recyclerItemViewMatcher(0, missionNameContentMatcher())
        //dateAtTimeTextView
        recyclerItemViewMatcher(0, dateTimeLabelMatcher())
        recyclerItemViewMatcher(0, dateAtTimeContentMatcher())
        //mission success
        recyclerItemViewMatcher(0, missionSuccessContentMatcher())
        //mission patch iv
        recyclerItemViewMatcher(0, missionPatchContentMatcher())
        //daysSinceFromTextView
        recyclerItemViewMatcher(0, daysSinceFromLabelMatcher())
        recyclerItemViewMatcher(0, daysSinceFromContentMatcher())
        //rocketTextView
        recyclerItemViewMatcher(0, rocketLabelMatcher())
        recyclerItemViewMatcher(0, rocketNameTypeContentMatcher())

    }


    @Test
    fun given_dataLoaded_WhenUserClicksFilter_ThenShowHeadersAndEmptyLaunchesAndCompanyData() {
        onView(filterButtonMatcher())
            .check(matches(hasContentDescription()))
            .check(matches(isDisplayed()))
            .perform(click())
    }

}
