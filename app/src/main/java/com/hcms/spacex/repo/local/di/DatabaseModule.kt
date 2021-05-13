package com.hcms.spacex.repo.local.di

import android.content.Context
import androidx.room.Room
import com.hcms.spacex.repo.local.CompanyInfoDatabase
import com.hcms.spacex.repo.local.DatabaseService
import com.hcms.spacex.repo.local.DatabaseServiceImpl
import com.hcms.spacex.repo.local.LaunchesDatabase
import com.hcms.spacex.repo.local.dao.CompanyInfoDAO
import com.hcms.spacex.repo.local.dao.LaunchItemDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideCompanyInfoDao(companyInfoDatabase: CompanyInfoDatabase): CompanyInfoDAO =
        companyInfoDatabase.companyInfoDao()

    @Provides
    fun provideLaunchItemDao(launchesDatabase: LaunchesDatabase): LaunchItemDAO =
        launchesDatabase.launchItemDao()

    @Provides
    fun provideDatabaseService(dbService: DatabaseServiceImpl): DatabaseService =
        dbService

    @Provides
    @Singleton
    fun provideCompanyInfoDB(@ApplicationContext applicationContext: Context): CompanyInfoDatabase {
        return Room.databaseBuilder(
            applicationContext,
            CompanyInfoDatabase::class.java,
            "companies"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLaunchesDB(@ApplicationContext applicationContext: Context): LaunchesDatabase {
        return Room.databaseBuilder(
            applicationContext,
            LaunchesDatabase::class.java,
            "launches"
        ).build()
    }
}