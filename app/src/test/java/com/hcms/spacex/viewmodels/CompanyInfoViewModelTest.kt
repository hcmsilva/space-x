package com.hcms.spacex.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hcms.spacex.R
import com.hcms.spacex.repo.Repository
import com.hcms.spacex.repo.RxImmediateSchedulerRule
import com.hcms.spacex.repo.TestHelper
import com.hcms.spacex.repo.local.domain.CompanyInfoDomain
import com.hcms.spacex.ui.utils.ResourceProvider
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class CompanyInfoViewModelTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var repo: Repository
    private lateinit var resourceProvider: ResourceProvider

    private lateinit var subject: CompanyInfoViewModel

    @Before
    fun setup() {
        repo = mockk()
        resourceProvider = mockk()
    }

    @Test
    fun `Given companyInfo success When getCompanyInfo invoked Then live data updated`() {
        val companyInfo: CompanyInfoDomain = mockk()
        TestHelper.commonSetupMockCompanyInfoDomain(companyInfo)
        every { repo.getCompanyInfo() } returns Flowable.just(listOf(companyInfo))
        every { resourceProvider.getString(R.string.company_info_template) } returns "%1\$s was founded by %2\$s in %3\$d. It has now %4\$d employees, %5\$d launch sites, and is valued at USD %6\$s"

        subject = CompanyInfoViewModel(repo, resourceProvider)
        subject.loadCompanyData()

        val expected =
            "SpaceX was founded by Elon Musk in 2002. It has now 7000 employees, 3 launch sites, and is valued at USD 27500000000"
        assertEquals(expected, subject.companyInfo.value)

    }

    //in a real app rather then not updating the ui both Error and Loading states would need attention
    @Test
    fun `Given companyInfo error When getCompanyInfo invoked Then No live data updated`() {
        val companyInfo: CompanyInfoDomain = mockk()
        TestHelper.commonSetupMockCompanyInfoDomain(companyInfo)
        every { repo.getCompanyInfo() } returns Flowable.just(emptyList())
        every { resourceProvider.getString(R.string.company_info_template) } returns "%1\$s was founded by %2\$s in %3\$d. It has now %4\$d employees, %5\$d launch sites, and is valued at USD %6\$s"

        subject = CompanyInfoViewModel(repo, resourceProvider)
        subject.loadCompanyData()

        val expected = ""
        assertEquals(expected, subject.companyInfo.value)
    }
}