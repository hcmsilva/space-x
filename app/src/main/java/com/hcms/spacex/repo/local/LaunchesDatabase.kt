package com.hcms.spacex.repo.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hcms.spacex.repo.local.dao.LaunchItemDAO
import com.hcms.spacex.repo.local.domain.LaunchItemDomain

@Database(entities = [LaunchItemDomain::class], version = 1)
abstract class LaunchesDatabase : RoomDatabase() {
    abstract fun launchItemDao(): LaunchItemDAO
}