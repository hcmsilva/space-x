package com.hcms.spacex.repo.local

import com.hcms.spacex.repo.local.dao.CompanyInfoDAO
import com.hcms.spacex.repo.local.dao.LaunchItemDAO
import com.hcms.spacex.repo.local.domain.CompanyInfoDomain
import com.hcms.spacex.repo.local.domain.LaunchItemDomain
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test

class DatabaseServiceImplTest {

    private lateinit var companyInfoDAO: CompanyInfoDAO
    private lateinit var launchItemDAO: LaunchItemDAO

    private lateinit var subject: DatabaseService

    @Before
    fun setUp() {
        companyInfoDAO = mockk()
        launchItemDAO = mockk()

        subject = DatabaseServiceImpl(companyInfoDAO, launchItemDAO)
    }

    @Test
    fun `Given companyInfo When save invoked Then CI data-access-object saveWithTimestamp is invoked with correct companyInfo`() {
        val companyInfo: CompanyInfoDomain = mockk()
        every { companyInfoDAO.saveWithTimestamp(companyInfo, any()) } returns mockk()

        subject.save(companyInfo)

        verify { companyInfoDAO.saveWithTimestamp(companyInfo, any()) }
    }

    @Test
    fun `Given launchesList When save invoked Then launches data-access-object saveWithTimestamp is invoked with same list`() {
        val launchesList: List<LaunchItemDomain> = mockk()
        every { launchItemDAO.saveWithTimestamp(launchesList, any()) } returns mockk()

        subject.save(launchesList)

        verify { launchItemDAO.saveWithTimestamp(launchesList, any()) }
    }

    @Test
    fun `Given company name When loadCompanyInfo invoked Then invoke companyInfo loadList and propagate Flowable stream`() {
        val companyName = "Android Ltd."
        val expectedCompany: CompanyInfoDomain = mockk()
        every { expectedCompany.name } returns companyName
        every { companyInfoDAO.loadList(companyName) } returns Flowable.just(listOf(expectedCompany))

        subject.loadCompanyInfo(companyName)
            .test()
            .assertValueCount(1)
            .assertValue(listOf(expectedCompany))
            .assertValue { list -> list.any { it.name == companyName } }

        verify { companyInfoDAO.loadList(companyName) }
    }

    @Test
    fun `When loadAllLaunches invoked Then invoke launchesDAO loadList and propagate Flowable stream`() {
        val listOfLaunches: List<LaunchItemDomain> = mockk()
        every { launchItemDAO.loadList() } returns Flowable.just(listOfLaunches)

        subject.loadAllLaunches()
            .test()
            .assertValueCount(1)
            .assertValue(listOfLaunches)

        verify { launchItemDAO.loadList() }
    }
}