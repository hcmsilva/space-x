package com.hcms.spacex.repo.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.hcms.spacex.repo.local.domain.LaunchItemDomain
import io.reactivex.Flowable

@Dao
abstract class LaunchItemDAO : TimestampedDAO<LaunchItemDomain>() {
//    @Insert(onConflict = REPLACE)
//    abstract fun save(companyInfo: CompanyInfoDomain): Completable

//    @Query("SELECT * FROM companies WHERE name = :companyName")
//    abstract fun load(companyName: String): Flowable<LaunchItemDomain>

    @Query("SELECT * FROM launches")
    abstract fun loadList(): Flowable<List<LaunchItemDomain>>
}