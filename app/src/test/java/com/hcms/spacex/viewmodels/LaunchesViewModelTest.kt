package com.hcms.spacex.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hcms.spacex.repo.Repository
import com.hcms.spacex.repo.RxImmediateSchedulerRule
import com.hcms.spacex.repo.TestHelper
import com.hcms.spacex.repo.local.domain.LaunchItemDomain
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class LaunchesViewModelTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var repo: Repository

    @Before
    fun setUp() {
        repo = mockk()
    }

    @Test
    fun `Given launches data When getAllLaunches invoked Then fullList and live data updated`() {
        val launchItem: LaunchItemDomain = mockk()
        TestHelper.commonSetupMockLaunchItemDomain(launchItem)
        val expectedList = listOf(launchItem)
        every { repo.getAllLaunches() } returns Flowable.just(expectedList)

        val subject = LaunchesViewModel(repo)

        assertEquals(expectedList, subject.launchList.value)
        assertEquals(expectedList, subject.fullList)
    }

    //in a real app rather then not updating the ui both Error and Loading states would need attention
    @Test
    fun `Given empty launches data When getAllLaunches invoked Then fullList and live data empty`() {
        val expectedList = listOf<LaunchItemDomain>()
        every { repo.getAllLaunches() } returns Flowable.just(expectedList)

        val subject = LaunchesViewModel(repo)

        assertEquals(expectedList, subject.launchList.value)
        assertEquals(expectedList, subject.fullList)
    }

    @Test
    fun `Given list of launches When filter by year Then filtered list only contains launches from that year`() {
        val initialList = getListOfMockLaunches()
        every { initialList[0].launchYear } returns "2023"
        every { initialList[1].launchYear } returns "2022"
        every { initialList[2].launchYear } returns "2021"

        every { repo.getAllLaunches() } returns Flowable.just(initialList)
        val expectedFilteredList = listOf(initialList[0])

        val subject = LaunchesViewModel(repo)

        subject.filter(year = "2023", filterSuccess = false, reversed = false)

        assertEquals(expectedFilteredList, subject.launchList.value)
    }

    @Test
    fun `Given list of launches in random order Then viewmodel outputs them in chronological order`() {
        val initialList = getListOfMockLaunches()
        every { initialList[0].launchYear } returns "2023"
        every { initialList[1].launchYear } returns "2021"
        every { initialList[2].launchYear } returns "2022"

        every { repo.getAllLaunches() } returns Flowable.just(initialList)
        val expectedFilteredList = listOf(initialList[1], initialList[2], initialList[0])

        val subject = LaunchesViewModel(repo)

        subject.filter(year = "", filterSuccess = false, reversed = false)

        assertEquals(expectedFilteredList, subject.launchList.value)
    }

    @Test
    fun `Given list of launches When filter by non-existent year Then filtered list is empty`() {
        val initialList = getListOfMockLaunches()
        every { initialList[0].launchYear } returns "2023"
        every { initialList[1].launchYear } returns "2022"
        every { initialList[2].launchYear } returns "2021"

        every { repo.getAllLaunches() } returns Flowable.just(initialList)
        val expectedFilteredList = listOf<LaunchItemDomain>()

        val subject = LaunchesViewModel(repo)

        subject.filter(year = "2024", filterSuccess = false, reversed = false)

        assertEquals(expectedFilteredList, subject.launchList.value)
    }

    @Test
    fun `Given list of launches When filter by success Then filtered list only contains launches which were successful`() {
        val initialList = getListOfMockLaunches()
        every { initialList[0].launchSuccess } returns false
        every { initialList[1].launchSuccess } returns true
        every { initialList[2].launchSuccess } returns true

        every { repo.getAllLaunches() } returns Flowable.just(initialList)
        val expectedFilteredList = listOf(initialList[1], initialList[2])

        val subject = LaunchesViewModel(repo)

        subject.filter(year = "", filterSuccess = true, reversed = false)

        assertEquals(expectedFilteredList, subject.launchList.value)
    }

    @Test
    fun `Given list of launches When filter sorted by DESC Then filtered list is reversed`() {
        val initialList = getListOfMockLaunches()
        every { initialList[0].launchYear } returns "2021"
        every { initialList[1].launchYear } returns "2022"
        every { initialList[2].launchYear } returns "2023"

        every { repo.getAllLaunches() } returns Flowable.just(initialList)
        val expectedFilteredList = listOf(initialList[2], initialList[1], initialList[0])

        val subject = LaunchesViewModel(repo)

        subject.filter(year = "", filterSuccess = false, reversed = true)

        assertEquals(expectedFilteredList, subject.launchList.value)
    }

    @Test
    fun `Given list of launches When filter sorted by DESC And filter And by success Then filtered list is reversed and successful`() {
        val initialList = getListOfMockLaunches()
        every { initialList[0].launchYear } returns "2021"
        every { initialList[0].launchSuccess } returns true
        every { initialList[1].launchYear } returns "2021"
        every { initialList[1].launchSuccess } returns false
        every { initialList[2].launchSuccess } returns true
        every { initialList[2].launchYear } returns "2023"

        every { repo.getAllLaunches() } returns Flowable.just(initialList)
        val expectedFilteredList = listOf(initialList[2], initialList[0])

        val subject = LaunchesViewModel(repo)

        subject.filter(year = "", filterSuccess = true, reversed = true)

        assertEquals(expectedFilteredList, subject.launchList.value)
    }

    @Test
    fun `Given previously filtered list When resetFilters invoked Then filtered list matches fullList`() {
        val initialList = getListOfMockLaunches()
        every { initialList[0].launchYear } returns "2021"
        every { initialList[1].launchYear } returns "2022"
        every { initialList[2].launchYear } returns "2023"
        every { repo.getAllLaunches() } returns Flowable.just(initialList)
        val subject = LaunchesViewModel(repo)
        subject.filter(year = "2024", filterSuccess = false, reversed = false)

        //list filtered
        assertEquals(emptyList<LaunchItemDomain>(), subject.launchList.value)

        subject.resetFilters()

        assertEquals(initialList, subject.launchList.value)
    }

    private fun getListOfMockLaunches(): List<LaunchItemDomain> {
        val launchItem1: LaunchItemDomain = mockk()
        TestHelper.commonSetupMockLaunchItemDomain(launchItem1)
        val launchItem2: LaunchItemDomain = mockk()
        TestHelper.commonSetupMockLaunchItemDomain(launchItem2)
        val launchItem3: LaunchItemDomain = mockk()
        TestHelper.commonSetupMockLaunchItemDomain(launchItem3)

        return listOf(launchItem1, launchItem2, launchItem3)
    }


}