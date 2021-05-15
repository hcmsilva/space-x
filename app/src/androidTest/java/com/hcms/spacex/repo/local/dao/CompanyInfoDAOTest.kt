package com.hcms.spacex.repo.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hcms.spacex.repo.local.dao.CompanyInfoDAO
import com.hcms.spacex.repo.local.domain.CompanyInfoDomain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CompanyInfoDAOTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: CompanyInfoDatabase
    private lateinit var subject: CompanyInfoDAO

    private val originalTimestamp: Long = 1L
    private val updatedTimestamp: Long = 1L + 1

    private val expectedCompanyInfo = CompanyInfoDomain(
        founder = "Elon Musk",
        founded = 2002,
        launchSites = 3,
        valuation = 27500000000,
        name = "SpaceX",
        employees = 7000,
        modifiedAt = originalTimestamp
    )

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CompanyInfoDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        subject = database.companyInfoDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun givenEmptyDb_WhenLoadInvoked_ThenNoValues() {
        subject
            .load("tesla")
            .test()
            .assertNoValues()
    }

    @Test
    fun givenEmptyDb_WhenSaveInvoked_ThenDataPersisted() {

        subject.save(expectedCompanyInfo).blockingAwait()

        val actualCompanyInfo = subject.load("SpaceX").blockingFirst()

        assertEquals(expectedCompanyInfo, actualCompanyInfo)
    }

    @Test
    fun givenEmptyDb_WhenSaveWithTimestampInvoked_ThenDataPersistedWithTimestamp() {
        subject.saveWithTimestamp(expectedCompanyInfo, originalTimestamp).blockingAwait()

        val actualCompanyInfo = subject.load("SpaceX").blockingFirst()

        assertEquals(expectedCompanyInfo, actualCompanyInfo)
        assertEquals(expectedCompanyInfo.modifiedAt, originalTimestamp)
    }

    @Test
    fun givenUpdate_WhenSaveInvoked_ThenDataUpdatePersisted() {

        subject.save(expectedCompanyInfo)
            .test()

        subject.load("SpaceX")
            .test()
            .assertValue(expectedCompanyInfo)

        val updatedInfo = expectedCompanyInfo.copy(founder = "Henrique")

        subject.save(updatedInfo)
            .test()

        subject.load("SpaceX")
            .test()
            .assertValue { it.founder == "Henrique" }
            .assertValue { it == updatedInfo }
    }

    @Test
    fun givenUpdate_WhenSaveWithTimestampInvoked_ThenDataUpdatePersisted() {

        subject.saveWithTimestamp(expectedCompanyInfo, originalTimestamp)
            .test()

        subject.load("SpaceX")
            .test()
            .assertValue(expectedCompanyInfo)

        val updatedInfo = expectedCompanyInfo.copy(founder = "Henrique")

        subject.saveWithTimestamp(updatedInfo, updatedTimestamp)
            .test()

        subject.load("SpaceX")
            .test()
            .assertValue { it.founder == "Henrique" }
            .assertValue { it == updatedInfo }
            .assertValue { it.modifiedAt == updatedTimestamp }
    }
}