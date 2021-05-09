package com.hcms.spacex.repo.remote

import com.hcms.spacex.repo.MockFileReader
import com.hcms.spacex.repo.RxImmediateSchedulerRule
import com.hcms.spacex.repo.remote.dto.CompanyInfoDTO
import com.hcms.spacex.repo.remote.dto.Headquarters
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
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

    private lateinit var resultsObserver: Observer<CompanyInfoDTO>
    private lateinit var testObserver: TestObserver<CompanyInfoDTO>

    private var searchResults: CompanyInfoDTO? = null

    @Before
    fun setup() {
        hiltRule.inject()
        resultsObserver = object : Observer<CompanyInfoDTO> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: CompanyInfoDTO) { searchResults = t }
            override fun onError(e: Throwable) { fail("api error") }
            override fun onComplete() { println("onComplete called") }
        }

        testObserver = TestObserver(resultsObserver)
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

    private fun prepareMockServerResponse(responseBodyPath: String, responseCode: Int) {
        val mockResponse = MockFileReader(responseBodyPath).content

        val response = MockResponse()
            .setResponseCode(responseCode)
            .setBody(mockResponse)

        server.start(8080)
        server.enqueue(response)
    }

}
