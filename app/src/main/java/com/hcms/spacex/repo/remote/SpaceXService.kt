package com.hcms.spacex.repo.remote

import com.hcms.spacex.repo.remote.dto.CompanyInfoDTO
import com.hcms.spacex.repo.remote.dto.LaunchItemDTO
import io.reactivex.Single
import retrofit2.http.GET

interface SpaceXService {
    @GET("v3/info")
    fun getCompanyInfo(): Single<CompanyInfoDTO>

    @GET("v3/launches")
    fun getAllLaunches(): Single<List<LaunchItemDTO>>
}