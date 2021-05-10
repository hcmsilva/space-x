package com.hcms.spacex.repo.local.di

import android.content.Context
import androidx.room.Room
import com.hcms.spacex.repo.local.CompanyInfoDAO
import com.hcms.spacex.repo.local.CompanyInfoDatabase
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
    @Singleton
    fun provideCompanyInfoDB(@ApplicationContext applicationContext: Context): CompanyInfoDatabase {
        return Room.databaseBuilder(
            applicationContext,
            CompanyInfoDatabase::class.java,
            "companies"
        ).build()
    }
}