package com.hcms.spacex.repo

import com.hcms.spacex.repo.local.DatabaseService
import com.hcms.spacex.repo.local.domain.CompanyInfoDomain
import com.hcms.spacex.repo.local.domain.LaunchItemDomain
import com.hcms.spacex.repo.remote.SpaceXService
import com.hcms.spacex.repo.remote.dto.CompanyInfoDTO
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    private lateinit var apiClient: SpaceXService
    private lateinit var dbClient: DatabaseService

    private lateinit var subject: Repository

    @Before
    fun setup() {
        apiClient = mockk()
        dbClient = mockk()

        subject = Repository(
            apiClient = apiClient,
            dbClient = dbClient
        )
    }

    @Test
    fun `Given previously cached companyInfo When repo getCompanyInfo invoked Then web service NOT invoked And db service save NOT invoked`() {
        val companyName = "Android Ltd."
        val cachedCompanyInfo: CompanyInfoDomain = mockk()

        every { dbClient.loadCompanyInfo(companyName) } returns Flowable.just(
            listOf(
                cachedCompanyInfo
            )
        )

        subject.getCompanyInfo(companyName)
            .test()
            .assertValueCount(1)

        verify { apiClient.getCompanyInfo() wasNot Called }
        verify { dbClient.save(any<CompanyInfoDomain>()) wasNot Called }
    }

    @Test
    fun `Given previously cached companyInfo When repo getCompanyInfo invoked Then cached data returned into flowable stream`() {
        val companyName = "Android Ltd."
        val cachedCompanyInfo: CompanyInfoDomain = mockk()

        every { dbClient.loadCompanyInfo(companyName) } returns Flowable.just(
            listOf(
                cachedCompanyInfo
            )
        )

        subject.getCompanyInfo(companyName)
            .test()
            .assertValue { it.contains(cachedCompanyInfo) }
    }

    @Test
    fun `Given NO previously cached companyInfo When repo getCompanyInfo invoked Then web service invoked And db service save invoked for caching`() {
        val companyName = "Android Ltd."
        val downloadedCompanyInfo: CompanyInfoDTO = mockk()

        commonSetupMockCompanyInfoDTO(downloadedCompanyInfo)

        every { dbClient.loadCompanyInfo(companyName) } returns Flowable.just(emptyList())
        every { apiClient.getCompanyInfo() } returns Single.just(downloadedCompanyInfo)
        every { dbClient.save(any<CompanyInfoDomain>()) } returns Completable.complete()

        subject.getCompanyInfo(companyName)
            .test()

        verify { apiClient.getCompanyInfo() }
        verify { dbClient.save(any<CompanyInfoDomain>()) }
    }

    @Test
    fun `Given NO previously cached companyInfo When repo getCompanyInfo invoked Then web service invoked company info returned into stream And cache updated`() {
        val companyName = "Android Ltd."
        val downloadedCompanyInfo: CompanyInfoDTO = mockk()
        commonSetupMockCompanyInfoDTO(downloadedCompanyInfo)
        val expectedResult: CompanyInfoDomain = mockk()
        setupExpectedResult(expectedResult)

        every { dbClient.loadCompanyInfo(companyName) } returns Flowable.just(emptyList())
        every { apiClient.getCompanyInfo() } returns Single.just(downloadedCompanyInfo)
        every { dbClient.save(any<CompanyInfoDomain>()) } returns Completable.complete()

        subject.getCompanyInfo(companyName)
            .test()
            .assertValue {
                it.any { info ->
                    info.name == expectedResult.name &&
                            info.founder == expectedResult.founder &&
                            info.founded == expectedResult.founded &&
                            info.launchSites == expectedResult.launchSites &&
                            info.valuation == expectedResult.valuation &&
                            info.employees == expectedResult.employees
                }
            }
    }

    @Test
    fun `Given stale cached companyInfo When repo getCompanyInfo invoked Then web service invoked And db service save invoked for caching`() {

    }

    @Test
    fun `Given previously cached launchItems When repo getCompanyInfo invoked Then web service NOT invoked And db service save NOT invoked`() {
        val cachedLaunchItem1: LaunchItemDomain = mockk()
        val cachedLaunchItem2: LaunchItemDomain = mockk()
        val cachedLaunchItem3: LaunchItemDomain = mockk()
        val cachedList = listOf(cachedLaunchItem1, cachedLaunchItem2, cachedLaunchItem3)

        every { dbClient.loadAllLaunches() } returns Flowable.just(cachedList)

        subject.getAllLaunches()
            .test()
            .assertValueCount(1)
            .assertValue { resultList -> resultList.containsAll(cachedList) }

        verify { apiClient.getAllLaunches() wasNot Called }
        verify { dbClient.save(any<List<LaunchItemDomain>>()) wasNot Called }
    }

    @Test
    fun `Given previously cached launchItems When repo getCompanyInfo invoked Then cached data returned into flowable stream`() {
        val cachedLaunchItem: LaunchItemDomain = mockk()

        every { dbClient.loadAllLaunches() } returns Flowable.just(listOf(cachedLaunchItem))

        subject.getAllLaunches()
            .test()
            .assertValueCount(1)

        verify { apiClient.getAllLaunches() wasNot Called }
        verify { dbClient.save(any<List<LaunchItemDomain>>()) wasNot Called }
    }

    @Test
    fun `Given NO previously cached launchItems When repo getCompanyInfo invoked Then web service invoked And db service save invoked for caching`() {

    }

    @Test
    fun `Given NO previously cached launchItems When repo getCompanyInfo invoked Then web service invoked company info returned into stream`() {

    }

    @Test
    fun `Given stale cached launchItems When repo getCompanyInfo invoked Then web service invoked And db service save invoked for caching`() {

    }


    private fun commonSetupMockCompanyInfoDTO(downloadedCompanyInfo: CompanyInfoDTO) {
        every { downloadedCompanyInfo.summary } returns "SpaceX designs, manufactures and launches advanced rockets and spacecraft. The company was founded in 2002 to revolutionize space technology, with the ultimate goal of enabling people to live on other planets."
        every { downloadedCompanyInfo.coo } returns "Gwynne Shotwell"
        every { downloadedCompanyInfo.founder } returns "Elon Musk"
        every { downloadedCompanyInfo.founded } returns 2002
        every { downloadedCompanyInfo.vehicles } returns 3
        every { downloadedCompanyInfo.ceo } returns "Elon Musk"
        every { downloadedCompanyInfo.launchSites } returns 3
        every { downloadedCompanyInfo.headquarters } returns null
        every { downloadedCompanyInfo.valuation } returns 27500000000
        every { downloadedCompanyInfo.name } returns "SpaceX"
        every { downloadedCompanyInfo.employees } returns 7000
        every { downloadedCompanyInfo.testSites } returns 1
        every { downloadedCompanyInfo.cto } returns "Elon Musk"
        every { downloadedCompanyInfo.ctoPropulsion } returns "Tom Mueller"

    }

    private fun setupExpectedResult(expectedSavedCache: CompanyInfoDomain) {
        every { expectedSavedCache.summary } returns "SpaceX designs, manufactures and launches advanced rockets and spacecraft. The company was founded in 2002 to revolutionize space technology, with the ultimate goal of enabling people to live on other planets."
        every { expectedSavedCache.coo } returns "Gwynne Shotwell"
        every { expectedSavedCache.founder } returns "Elon Musk"
        every { expectedSavedCache.founded } returns 2002
        every { expectedSavedCache.vehicles } returns 3
        every { expectedSavedCache.ceo } returns "Elon Musk"
        every { expectedSavedCache.launchSites } returns 3
        every { expectedSavedCache.headquarters } returns null
        every { expectedSavedCache.valuation } returns 27500000000
        every { expectedSavedCache.name } returns "SpaceX"
        every { expectedSavedCache.employees } returns 7000
        every { expectedSavedCache.testSites } returns 1
        every { expectedSavedCache.cto } returns "Elon Musk"
        every { expectedSavedCache.ctoPropulsion } returns "Tom Mueller"
        every { expectedSavedCache.modifiedAt } returns 0L
    }

    private fun buildDummyCompanyInfoDto() = CompanyInfoDTO(
        summary = "SpaceX designs, manufactures and launches advanced rockets and spacecraft. The company was founded in 2002 to revolutionize space technology, with the ultimate goal of enabling people to live on other planets.",
        coo = "Gwynne Shotwell",
        founder = "Elon Musk",
        founded = 2002,
        vehicles = 3,
        ceo = "Elon Musk",
        launchSites = 3,
        headquarters = null,
        valuation = 27500000000,
        name = "SpaceX",
        employees = 7000,
        testSites = 1,
        cto = "Elon Musk",
        ctoPropulsion = "Tom Mueller"
    )
}