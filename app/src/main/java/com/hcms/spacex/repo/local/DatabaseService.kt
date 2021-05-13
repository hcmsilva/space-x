package com.hcms.spacex.repo.local

import com.hcms.spacex.repo.local.dao.CompanyInfoDAO
import com.hcms.spacex.repo.local.dao.LaunchItemDAO
import com.hcms.spacex.repo.local.domain.CompanyInfoDomain
import com.hcms.spacex.repo.local.domain.LaunchItemDomain
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

interface DatabaseService {
    fun save(companyInfo: CompanyInfoDomain): Completable
    fun save(allLaunches: List<LaunchItemDomain>): Completable
    fun loadCompanyInfo(companyName: String): Flowable<List<CompanyInfoDomain>>
    fun loadAllLaunches(): Flowable<List<LaunchItemDomain>>
}

class DatabaseServiceImpl @Inject constructor(
    private val companyInfoDAO: CompanyInfoDAO,
    private val launchesDAO: LaunchItemDAO
) : DatabaseService {
    override fun save(companyInfo: CompanyInfoDomain): Completable =
        companyInfoDAO.save(companyInfo)

    override fun save(allLaunches: List<LaunchItemDomain>): Completable =
        launchesDAO.saveWithTimestamp(allLaunches)

    override fun loadCompanyInfo(companyName: String): Flowable<List<CompanyInfoDomain>> =
        companyInfoDAO.loadList(companyName)

    override fun loadAllLaunches(): Flowable<List<LaunchItemDomain>> =
        launchesDAO.loadList()
}