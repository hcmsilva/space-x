package com.hcms.spacex.repo.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.hcms.spacex.repo.local.domain.CompanyInfoDomain
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface CompanyInfoDAO {
    @Insert(onConflict = REPLACE)
    fun save(companyInfo: CompanyInfoDomain): Completable

    @Query("SELECT * FROM companies WHERE name = :companyName")
    fun load(companyName: String): Flowable<CompanyInfoDomain>
}