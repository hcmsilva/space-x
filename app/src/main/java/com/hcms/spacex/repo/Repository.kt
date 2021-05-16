package com.hcms.spacex.repo


import com.hcms.spacex.repo.local.DatabaseService
import com.hcms.spacex.repo.local.domain.CompanyInfoDomain
import com.hcms.spacex.repo.local.domain.LaunchItemDomain
import com.hcms.spacex.repo.mappers.toDomain
import com.hcms.spacex.repo.remote.SpaceXService
import com.hcms.spacex.repo.remote.dto.CompanyInfoDTO
import com.hcms.spacex.repo.remote.dto.LaunchItemDTO
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiClient: SpaceXService,
    private val dbClient: DatabaseService,
    private val cacheValidator: CacheValidator
) : ICompanyInfoRepo, ILaunchesRepo {
    private val COMPANY_NAME = "SpaceX"

    override fun getCompanyInfo(): Flowable<List<CompanyInfoDomain>> {
        return dbClient.loadCompanyInfo(COMPANY_NAME)
            .flatMap { localData ->
                if (localData.isEmpty() || cacheValidator.cacheIsStale(localData)) {
                    remoteLoadAndCacheCompanyInfo()
                } else {
                    Flowable.just(localData)
                }
            }
    }

    override fun getAllLaunches(): Flowable<List<LaunchItemDomain>> {
        return dbClient.loadAllLaunches()
            .flatMap { localData ->
                if (localData.isEmpty() || cacheValidator.cacheIsStale(localData)) {
                    remoteLoadAndCacheAllLaunches()
                } else {
                    Flowable.just(localData)
                }
            }
    }

    private fun remoteLoadAndCacheCompanyInfo(): Flowable<List<CompanyInfoDomain>> =
        apiClient.getCompanyInfo()
            .doAfterSuccess { saveDTOLocally(it) }
            .map { item -> listOf(item.toDomain()) }
            .toFlowable()

    private fun remoteLoadAndCacheAllLaunches(): Flowable<List<LaunchItemDomain>> =
        apiClient.getAllLaunches()
            .doAfterSuccess { saveDTOLocally(it) }
            .map { item -> item.toDomain() }
            .toFlowable()

    private fun saveDTOLocally(companyInfo: CompanyInfoDTO) {
        dbClient.save(companyInfo.toDomain())
            .subscribeOn(Schedulers.io())
            .subscribe()
    }


    private fun saveDTOLocally(launchItemDTOList: List<LaunchItemDTO>) {
        dbClient.save(launchItemDTOList.toDomain())
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

}


interface ICompanyInfoRepo {
    fun getCompanyInfo(): Flowable<List<CompanyInfoDomain>>
}

interface ILaunchesRepo {
    fun getAllLaunches(): Flowable<List<LaunchItemDomain>>
}