package com.hcms.spacex.repo.mappers

import com.hcms.spacex.repo.local.domain.CompanyInfoDomain
import com.hcms.spacex.repo.local.domain.LaunchItemDomain
import com.hcms.spacex.repo.remote.dto.CompanyInfoDTO
import com.hcms.spacex.repo.remote.dto.LaunchItemDTO

internal fun CompanyInfoDTO.toDomain(): CompanyInfoDomain {
    return CompanyInfoDomain(
        name = name,
        summary = summary,//
        coo = coo,//
        founder = founder,
        founded = founded,
        vehicles = vehicles,//
        ceo = ceo,//
        launchSites = launchSites,
        valuation = valuation,
        employees = employees,
        modifiedAt = System.currentTimeMillis()
    )
}

internal fun List<LaunchItemDTO>.toDomain(): List<LaunchItemDomain> =
    this.map { it.toDomain() }

internal fun LaunchItemDTO.toDomain(): LaunchItemDomain =
    LaunchItemDomain(
        missionName = missionName ?: "",
        launchYear = launchYear,
        launchDateUtc = launchDateUtc,
        launchDateUnix = launchDateUnix,
        rocketType = rocket?.rocketType,
        rocketName = rocket?.rocketName,
        launchSuccess = launchSuccess,
        wikipedia = links?.wikipedia,
        videoLink = links?.videoLink,
        missionPatchSmall = links?.missionPatchSmall,
        upcoming = upcoming,
        modifiedAt = System.currentTimeMillis()
    )