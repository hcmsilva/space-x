package com.hcms.spacex.repo.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hcms.spacex.repo.local.domain.CompanyInfoDomain

@Database(entities = [CompanyInfoDomain::class], version = 1)
abstract class CompanyInfoDatabase: RoomDatabase() {
    abstract fun companyInfoDao(): CompanyInfoDAO
}