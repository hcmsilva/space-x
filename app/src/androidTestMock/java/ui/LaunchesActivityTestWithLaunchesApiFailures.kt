package com.hcms.spacex.ui


import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.hcms.spacex.SpaceXApplication
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
class LaunchesActivityTestWithLaunchesApiFailures {

    @get:Rule
    var activityRule: ActivityScenarioRule<LaunchesActivity> =
        ActivityScenarioRule(LaunchesActivity::class.java)


    companion object {
        private val localWebServer = MockWebServer()

        @JvmStatic
        @BeforeClass
        fun serverSetup() {

            ApplicationProvider.getApplicationContext<SpaceXApplication>()
                .deleteDatabase("launches")

            //mock server dispatcher setup for this feature
            val errorDispatcher: Dispatcher = object : Dispatcher() {

                override fun dispatch(request: RecordedRequest): MockResponse {
                    val errorResponse =
                        MockResponse().setResponseCode(400)
                    val companyInfoRecordedResponse =
                        MockFileReader("/company_info_mock_response.json").content

                    when (request.path) {
                        "/v3/info" -> return MockResponse().setBody(companyInfoRecordedResponse)
                            .setResponseCode(200)
                        "/v3/launches" -> return errorResponse

                    }
                    return MockResponse().setResponseCode(400)
                }
            }

            IdlingRegistry.getInstance()
                .register(CountingIdlingResourceSingleton.countingIdlingResCompanyInfo)
            IdlingRegistry.getInstance()
                .register(CountingIdlingResourceSingleton.countingIdlingResAllLaunches)


            localWebServer.dispatcher = errorDispatcher
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
    fun givenLaunchesApiErrorAndNoCache_AndCompanyInfoLoadSuccessfullyWhenActivityStarted_ThenShowHeadersAndEmptyLaunchesDataAndValidCompanyInfo() {


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

    }

}
