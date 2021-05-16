package com.hcms.spacex.repo

import com.hcms.spacex.repo.local.DatabaseService
import com.hcms.spacex.repo.local.domain.CompanyInfoDomain
import com.hcms.spacex.repo.local.domain.LaunchItemDomain
import com.hcms.spacex.repo.remote.SpaceXService
import com.hcms.spacex.repo.remote.dto.CompanyInfoDTO
import com.hcms.spacex.repo.remote.dto.LaunchItemDTO
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
    private lateinit var cacheValidator: CacheValidator
    private lateinit var companyName: String

    private lateinit var subject: Repository

    @Before
    fun setup() {
        apiClient = mockk()
        dbClient = mockk()
        cacheValidator = mockk()

        every { cacheValidator.cacheIsStale(any<List<CompanyInfoDomain>>()) } returns false
        every { cacheValidator.cacheIsStale(any<List<LaunchItemDomain>>()) } returns false

        subject = Repository(
            apiClient = apiClient,
            dbClient = dbClient,
            cacheValidator = cacheValidator
        )
        companyName = subject.COMPANY_NAME
    }

    @Test
    fun `Given previously cached companyInfo When repo getCompanyInfo invoked Then web service NOT invoked And db service save NOT invoked`() {

        val cachedCompanyInfo: CompanyInfoDomain = mockk()

        every { dbClient.loadCompanyInfo(companyName) } returns Flowable.just(
            listOf(
                cachedCompanyInfo
            )
        )

        subject.getCompanyInfo()
            .test()
            .assertValueCount(1)

        verify { apiClient.getCompanyInfo() wasNot Called }
        verify { dbClient.save(any<CompanyInfoDomain>()) wasNot Called }
    }

    @Test
    fun `Given previously cached companyInfo When repo getCompanyInfo invoked Then cached data returned into flowable stream`() {

        val cachedCompanyInfo: CompanyInfoDomain = mockk()

        every { dbClient.loadCompanyInfo(companyName) } returns Flowable.just(
            listOf(
                cachedCompanyInfo
            )
        )

        subject.getCompanyInfo()
            .test()
            .assertValue { it.contains(cachedCompanyInfo) }
    }

    @Test
    fun `Given NO previously cached companyInfo When repo getCompanyInfo invoked Then web service invoked And db service save invoked for caching`() {

        val downloadedCompanyInfo: CompanyInfoDTO = mockk()
        TestHelper.commonSetupMockCompanyInfoDTO(downloadedCompanyInfo)

        every { dbClient.loadCompanyInfo(companyName) } returns Flowable.just(emptyList())
        every { apiClient.getCompanyInfo() } returns Single.just(downloadedCompanyInfo)
        every { dbClient.save(any<CompanyInfoDomain>()) } returns Completable.complete()

        subject.getCompanyInfo()
            .test()

        verify { apiClient.getCompanyInfo() }
        verify { dbClient.save(any<CompanyInfoDomain>()) }
    }

    @Test
    fun `Given NO previously cached companyInfo When repo getCompanyInfo invoked Then web service invoked company info returned into stream And cache updated`() {

        val downloadedCompanyInfo: CompanyInfoDTO = mockk()
        TestHelper.commonSetupMockCompanyInfoDTO(downloadedCompanyInfo)
        val expectedResult: CompanyInfoDomain = mockk()
        TestHelper.commonSetupMockCompanyInfoDomain(expectedResult)

        every { dbClient.loadCompanyInfo(companyName) } returns Flowable.just(emptyList())
        every { apiClient.getCompanyInfo() } returns Single.just(downloadedCompanyInfo)
        every { dbClient.save(any<CompanyInfoDomain>()) } returns Completable.complete()

        subject.getCompanyInfo()
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

        val cachedCompanyInfo: CompanyInfoDomain = mockk()

        every { dbClient.loadCompanyInfo(companyName) } returns Flowable.just(
            listOf(
                cachedCompanyInfo
            )
        )
        val downloadedCompanyInfo: CompanyInfoDTO = mockk()
        TestHelper.commonSetupMockCompanyInfoDTO(downloadedCompanyInfo)
        every { cacheValidator.cacheIsStale(listOf(cachedCompanyInfo)) } returns true
        every { apiClient.getCompanyInfo() } returns Single.just(downloadedCompanyInfo)
        every { dbClient.save(any<CompanyInfoDomain>()) } returns Completable.complete()

        subject.getCompanyInfo()
            .test()

        verify { apiClient.getCompanyInfo() }
        verify { dbClient.save(any<CompanyInfoDomain>()) }
    }

    @Test
    fun `Given previously cached launchItems When repo getAllLaunches invoked Then web service NOT invoked And db service save NOT invoked`() {
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
    fun `Given previously cached launchItems When repo getAllLaunches invoked Then cached data returned into flowable stream`() {
        val cachedLaunchItem: LaunchItemDomain = mockk()

        every { dbClient.loadAllLaunches() } returns Flowable.just(listOf(cachedLaunchItem))

        subject.getAllLaunches()
            .test()
            .assertValueCount(1)

        verify { apiClient.getAllLaunches() wasNot Called }
        verify { dbClient.save(any<List<LaunchItemDomain>>()) wasNot Called }
    }

    @Test
    fun `Given NO previously cached launchItems When repo getAllLaunches invoked Then web service invoked And db service save invoked for caching`() {
        val downloadedLaunchItem: LaunchItemDTO = mockk()
        TestHelper.commonSetupMockLaunchItemDTO(downloadedLaunchItem)

        every { dbClient.loadAllLaunches() } returns Flowable.just(emptyList())
        every { apiClient.getAllLaunches() } returns Single.just(listOf(downloadedLaunchItem))
        every { dbClient.save(any<List<LaunchItemDomain>>()) } returns Completable.complete()

        subject.getAllLaunches()
            .test()
            .assertValueCount(1)

        verify { apiClient.getAllLaunches() }
        verify { dbClient.save(any<List<LaunchItemDomain>>()) }
    }

    @Test
    fun `Given NO previously cached launchItems When repo getAllLaunches invoked Then web service invoked company info returned into stream`() {
        val downloadedLaunchItem: LaunchItemDTO = mockk()
        TestHelper.commonSetupMockLaunchItemDTO(downloadedLaunchItem)
        val expectedResult: LaunchItemDomain = mockk()
        TestHelper.commonSetupMockLaunchItemDomain(expectedResult)

        every { dbClient.loadAllLaunches() } returns Flowable.just(emptyList())
        every { apiClient.getAllLaunches() } returns Single.just(listOf(downloadedLaunchItem))
        every { dbClient.save(any<List<LaunchItemDomain>>()) } returns Completable.complete()

        subject.getAllLaunches()
            .test()
            .assertValueCount(1)
            .assertValue {
                it.any { launch ->
                    launch.missionName == expectedResult.missionName &&
                            launch.launchYear == expectedResult.launchYear &&
                            launch.rocketType == expectedResult.rocketType &&
                            launch.rocketName == expectedResult.rocketName &&
                            launch.launchSuccess == expectedResult.launchSuccess &&
                            launch.wikipedia == expectedResult.wikipedia &&
                            launch.videoLink == expectedResult.videoLink &&
                            launch.missionPatchSmall == expectedResult.missionPatchSmall &&
                            launch.upcoming == expectedResult.upcoming
                }
            }
    }

    @Test
    fun `Given stale cached launchItems When repo getAllLaunches invoked Then web service invoked And db service save invoked for caching`() {
        val cachedLaunchItem1: LaunchItemDomain = mockk()
        val cachedList = listOf(cachedLaunchItem1)
        val downloadedLaunchItem: LaunchItemDTO = mockk()
        TestHelper.commonSetupMockLaunchItemDTO(downloadedLaunchItem)
        every { dbClient.loadAllLaunches() } returns Flowable.just(cachedList)
        every { cacheValidator.cacheIsStale(cachedList) } returns true
        every { apiClient.getAllLaunches() } returns Single.just(listOf(downloadedLaunchItem))
        every { dbClient.save(any<List<LaunchItemDomain>>()) } returns Completable.complete()

        subject.getAllLaunches()
            .test()
            .assertValueCount(1)

        verify { apiClient.getAllLaunches() }
        verify { dbClient.save(any<List<LaunchItemDomain>>()) }
    }
}