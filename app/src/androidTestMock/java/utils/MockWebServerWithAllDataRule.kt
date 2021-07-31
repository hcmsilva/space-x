package utils

import com.hcms.spacex.repo.MockFileReader
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class MockWebServerWithAllDataRule : TestRule {
    override fun apply(base: Statement, description: Description?): Statement =
        MockWebServerWithAllDataSuccessStatement(base)
}

class MockWebServerWithAllDataSuccessStatement(private val base: Statement) : Statement() {
    lateinit var localWebServer: MockWebServer

    override fun evaluate() {
        //@Before - setup
        serverSetupWithAllDataSuccess()
        try {
            base.evaluate()
        } finally {
            //@After - cleanup
            serverTearDown()
        }
    }

    private fun serverSetupWithAllDataSuccess() {
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

        localWebServer = MockWebServer()
        localWebServer.dispatcher = dispatcher
        localWebServer.start(8080)
    }

    private fun serverTearDown() {
        localWebServer.shutdown()
    }

}