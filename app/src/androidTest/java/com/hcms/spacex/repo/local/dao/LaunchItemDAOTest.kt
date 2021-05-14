package com.hcms.spacex.repo.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hcms.spacex.repo.local.LaunchesDatabase
import com.hcms.spacex.repo.local.domain.LaunchItemDomain
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LaunchItemDAOTest : TestCase() {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: LaunchesDatabase
    private lateinit var subject: LaunchItemDAO

    private val originalTimestamp: Long = 1L
    private val updatedTimestamp: Long = 1L + 1

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LaunchesDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        subject = database.launchItemDao()
    }

    @After
    fun closedDb() {
        database.close()
    }

    @Test
    fun givenEmptyDb_WhenLoadInvoked_ThenNoValues() {
        subject.loadList()
            .test()
            .assertValue { result -> result == emptyList<LaunchItemDomain>() }
    }

    @Test
    fun givenEmptyDb_WhenSaveInvoked_ThenDataPersisted() {
        subject.loadList()
            .test()
            .assertValue { result -> result == emptyList<LaunchItemDomain>() }

        val expectedLaunch = buildDummyLaunch()
        subject.save(expectedLaunch).blockingAwait()

        subject.loadList()
            .test()
            .assertValue {
                it.contains(expectedLaunch)
            }
    }

    @Test
    fun givenEmptyDb_WhenSaveWithTimestampInvoked_ThenDataPersistedWithTimestamp() {
        subject.loadList()
            .test()
            .assertValue { result -> result == emptyList<LaunchItemDomain>() }

        val expectedLaunch = buildDummyLaunch()
        subject.saveWithTimestamp(
            data = expectedLaunch,
            timestamp = originalTimestamp
        ).blockingAwait()

        subject.loadList()
            .test()
            .assertValue { it ->
                it.contains(expectedLaunch)
                it.any { launch -> launch.modifiedAt == originalTimestamp }
            }
    }

    @Test
    fun givenUpdate_WhenSaveInvoked_ThenDataUpdatePersisted() {

        subject.loadList()
            .test()

        val expectedLaunch = buildDummyLaunch()
        subject.save(expectedLaunch).blockingAwait()

        subject.loadList()
            .test()
            .assertValue {
                it.contains(expectedLaunch)
            }

        val updatedLaunch = expectedLaunch.copy(launchSuccess = true)

        subject.saveWithTimestamp(updatedLaunch).blockingAwait()

        subject.loadList()
            .test()
            .assertValue { launchItems -> launchItems.none { it == expectedLaunch } }
            .assertValue { launchItems -> launchItems.contains(updatedLaunch) }
    }

    @Test
    fun givenUpdate_WhenSaveWithTimestampInvoked_ThenDataUpdatePersisted() {

        subject.loadList()
            .test()

        val expectedLaunch = buildDummyLaunch()
        subject.saveWithTimestamp(
            data = expectedLaunch,
            timestamp = originalTimestamp
        ).blockingAwait()

        subject.loadList()
            .test()
            .assertValue {
                it.contains(expectedLaunch)
                it.any { launch -> launch.modifiedAt == originalTimestamp }
            }

        val updatedLaunch = expectedLaunch.copy(launchSuccess = true)

        subject.saveWithTimestamp(
            data = updatedLaunch,
            timestamp = updatedTimestamp
        ).blockingAwait()

        subject.loadList()
            .test()
            .assertValue { launchItems -> launchItems.none { it == expectedLaunch } }
            .assertValue { launchItems -> launchItems.contains(updatedLaunch) }
            .assertValue { launchItems -> launchItems.any { it.modifiedAt == updatedTimestamp } }

    }

    @Test
    fun givenEmptyDbAndListOfLaunches_WhenSaveWithTimestampInvoked_ThenEntireListPersisted() {

        val expectedLaunch1 = buildDummyLaunch()
        val expectedLaunch2 = expectedLaunch1.copy(missionName = "second")
        val expectedLaunch3 = expectedLaunch1.copy(missionName = "third")
        val allExpectedLaunches = listOf(expectedLaunch1, expectedLaunch2, expectedLaunch3)

        subject.saveWithTimestamp(
            data = allExpectedLaunches,
            timestamp = originalTimestamp
        ).blockingAwait()

        subject.loadList()
            .test()
            .assertValue {
                it.containsAll(allExpectedLaunches)
                it.all { launch -> launch.modifiedAt == originalTimestamp }
            }
    }

    @Test
    fun givenListOfLaunchesAndUpdate_WhenSaveWithTimestampInvoked_ThenEntireListPersistedWithNewTimestamp() {

        val expectedLaunch1 = buildDummyLaunch()
        val expectedLaunch2 = expectedLaunch1.copy(missionName = "second")
        val expectedLaunch3 = expectedLaunch1.copy(missionName = "third")
        val allOriginallyExpectedLaunches =
            listOf(expectedLaunch1, expectedLaunch2, expectedLaunch3)

        subject.saveWithTimestamp(
            data = allOriginallyExpectedLaunches,
            timestamp = originalTimestamp
        ).blockingAwait()

        subject.loadList()
            .test()
            .assertValue {
                it.containsAll(allOriginallyExpectedLaunches)
                it.all { launch -> launch.modifiedAt == originalTimestamp }
            }


        val updatedLaunch1 = buildDummyLaunch().copy(launchSuccess = true)
        val updatedLaunch2 = expectedLaunch2.copy(launchSuccess = true)
        val updatedLaunch3 = expectedLaunch3.copy(launchSuccess = true)
        val updatedLaunchList = listOf(updatedLaunch1, updatedLaunch2, updatedLaunch3)

        subject.saveWithTimestamp(
            data = updatedLaunchList,
            timestamp = updatedTimestamp
        ).blockingAwait()

        subject.loadList()
            .test()
            .assertValue { it != allOriginallyExpectedLaunches }
            .assertValue { it == updatedLaunchList }
            .assertValue { launchItems -> launchItems.all { it.modifiedAt == updatedTimestamp } }

    }


    private fun buildDummyLaunch() =
        LaunchItemDomain(
            missionName = "some name",
            launchYear = "2021",
            launchDateUtc = "2006-03-24T22:30:00.000Z",
            launchDateUnix = 1143239400,
            rocketType = "Merlin A",
            rocketName = "Falcon 1",
            launchSuccess = false,
            wikipedia = "https://en.wikipedia.org/wiki/DemoSat",
            missionPatchSmall = "https://images2.imgbox.com/3c/0e/T8iJcSN3_o.png",
            videoLink = "https://www.youtube.com/watch?v=0a_00nJ_Y88",
            upcoming = false,
            modifiedAt = originalTimestamp
        )
}
