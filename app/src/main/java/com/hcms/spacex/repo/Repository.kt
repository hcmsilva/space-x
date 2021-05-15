package com.hcms.spacex.repo


import com.hcms.spacex.repo.local.DatabaseService
import com.hcms.spacex.repo.local.domain.CompanyInfoDomain
import com.hcms.spacex.repo.local.domain.LaunchItemDomain
import com.hcms.spacex.repo.mappers.toDomain
import com.hcms.spacex.repo.remote.SpaceXService
import com.hcms.spacex.repo.remote.dto.CompanyInfoDTO
import com.hcms.spacex.repo.remote.dto.LaunchItemDTO
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject


class Repository @Inject constructor(
    private val apiClient: SpaceXService,
    private val dbClient: DatabaseService
) : IRepo {

    override fun getCompanyInfo(companyName: String): Flowable<List<CompanyInfoDomain>> {
        return dbClient.loadCompanyInfo(companyName)
            .flatMap { localData ->
                if (localData.isEmpty()) {
                    remoteLoadAndCacheCompanyInfo()
                } else {
                    Flowable.just(localData)
                }
            }
    }

    override fun getAllLaunches(): Flowable<List<LaunchItemDomain>> {
        return dbClient.loadAllLaunches()
            .flatMap { localData ->
                if (localData.isEmpty()) {
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

    private fun saveDTOLocally(companyInfo: CompanyInfoDTO): Completable =
        dbClient.save(companyInfo.toDomain())

    private fun saveDTOLocally(launchItemDTOList: List<LaunchItemDTO>): Completable =
        dbClient.save(launchItemDTOList.toDomain())


}

interface IRepo {
    fun getCompanyInfo(companyName: String): Flowable<List<CompanyInfoDomain>>
    fun getAllLaunches(): Flowable<List<LaunchItemDomain>>
}