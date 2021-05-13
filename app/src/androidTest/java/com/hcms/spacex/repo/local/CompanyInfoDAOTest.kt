package com.hcms.spacex.repo.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hcms.spacex.repo.local.dao.CompanyInfoDAO
import com.hcms.spacex.repo.local.domain.CompanyInfoDomain
import com.hcms.spacex.repo.local.domain.HeadquartersDomain
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

    val originalTimestamp: Long = 1L
    val updatedTimestamp: Long = 1L+1
    private val expectedHQ = HeadquartersDomain(
        address = "Rocket Road",
        city = "Hawthorne",
        state = "California"
    )
    private val expectedCompanyInfo = CompanyInfoDomain(
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
        ctoPropulsion = "Tom Mueller",
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

        val updatedInfo = expectedCompanyInfo.copy(ceo = "Henrique")

        subject.save(updatedInfo)
            .test()

        subject.load("SpaceX")
            .test()
            .assertValue { it.ceo == "Henrique" }
            .assertValue { it == updatedInfo }
    }

    @Test
    fun givenUpdate_WhenSaveWithTimestampInvoked_ThenDataUpdatePersisted() {

        subject.saveWithTimestamp(expectedCompanyInfo, originalTimestamp)
            .test()

        subject.load("SpaceX")
            .test()
            .assertValue(expectedCompanyInfo)

        val updatedInfo = expectedCompanyInfo.copy(ceo = "Henrique")

        subject.saveWithTimestamp(updatedInfo, updatedTimestamp)
            .test()

        subject.load("SpaceX")
            .test()
            .assertValue { it.ceo == "Henrique" }
            .assertValue { it == updatedInfo }
            .assertValue { it.modifiedAt == updatedTimestamp }
    }
}