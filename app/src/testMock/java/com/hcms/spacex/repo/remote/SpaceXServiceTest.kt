package com.hcms.spacex.repo.remote

import com.hcms.spacex.repo.MockFileReader
import com.hcms.spacex.repo.RxImmediateSchedulerRule
import com.hcms.spacex.repo.remote.dto.CompanyInfoDTO
import com.hcms.spacex.repo.remote.dto.Headquarters
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

//should only be done in a local controlled environment and a mock webserver

@HiltAndroidTest
@Config(application = HiltTestApplication::class, sdk = [28])
@RunWith(RobolectricTestRunner::class)
class SpaceXServiceTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var server: MockWebServer

    @Inject
    lateinit var subject: SpaceXService

    @Before
    fun setup() {
        hiltRule.inject()
        server = MockWebServer()
    }

    @After
    fun tearDownMockServer() {
        server.shutdown()
    }

    @Test
    fun `Given json mockResponse When getCompanyInfo invoked Then DTO matches json contents`() {

        prepareMockServerResponse(
            responseBodyPath = "/company_info_mock_response.json",
            responseCode = 200
        )

        val expectedHQ = Headquarters(
            address = "Rocket Road",
            city = "Hawthorne",
            state = "California"
        )
        val expectedResult = CompanyInfoDTO(
            summary = "SpaceX designs, manufactures and launches advanced rockets and spacecraft. The company was founded in 2002 to revolutionize space technology, with the ultimate goal of enabling people to live on other planets.",
            coo = "Gwynne Shotwell",
            founder = "Elon Musk",
            founded = 2002,
            vehicles = 3,
            ceo = "Elon Musk",
            launchSites = 3,
            headquarters = expectedHQ,
            valuation = 27500000000,
            name = "SpaceX",
            employees = 7000,
            testSites = 1,
            cto = "Elon Musk",
            ctoPropulsion = "Tom Mueller"
        )

        val actualResult = subject.getCompanyInfo()
            .blockingGet()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given json mockResponse When getAllLaunches invoked Then DTO matches json contents`() {
        val expectedLaunchesNumber = 111
        val expectedFirstMissionName = "FalconSat"
        val expectedLastMissionName = "SXM-7"
        prepareMockServerResponse(
            responseBodyPath = "/all_launches_mock_response.json",
            responseCode = 200
        )

        val actualResult = subject.getAllLaunches().blockingGet()
        val actualFirstMissionName = actualResult.first()?.missionName
        val actualLastMissionName = actualResult.last()?.missionName

        assertEquals(expectedLaunchesNumber, actualResult.size)
        assertEquals(expectedFirstMissionName, actualFirstMissionName)
        assertEquals(expectedLastMissionName, actualLastMissionName)
    }

    private fun prepareMockServerResponse(responseBodyPath: String, responseCode: Int) {
        val mockResponse = MockFileReader(responseBodyPath).content

        val response = MockResponse()
            .setResponseCode(responseCode)
            .setBody(mockResponse)

        server.start(8080)
        server.enqueue(response)
    }

}
