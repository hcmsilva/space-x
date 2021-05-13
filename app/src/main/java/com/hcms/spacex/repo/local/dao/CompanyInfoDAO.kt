package com.hcms.spacex.repo.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.hcms.spacex.repo.local.domain.CompanyInfoDomain
import io.reactivex.Flowable

@Dao
abstract class CompanyInfoDAO : TimestampedDAO<CompanyInfoDomain>() {
//    @Insert(onConflict = REPLACE)
//    abstract fun save(companyInfo: CompanyInfoDomain): Completable

    @Query("SELECT * FROM companies WHERE name = :companyName")
    abstract fun load(companyName: String): Flowable<CompanyInfoDomain>

    @Query("SELECT * FROM companies WHERE name = :companyName")
    abstract fun loadList(companyName: String): Flowable<List<CompanyInfoDomain>>
}