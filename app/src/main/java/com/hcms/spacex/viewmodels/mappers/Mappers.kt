package com.hcms.spacex.viewmodels.mappers

import com.hcms.spacex.repo.local.domain.LaunchItemDomain
import com.hcms.spacex.ui.utils.ResourceProvider
import com.hcms.spacex.viewmodels.LaunchItemViewModel

fun List<LaunchItemDomain>.toLaunchItemList(resourceProvider: ResourceProvider): List<LaunchItemViewModel> =
    this.map { launchItemDomain ->
        LaunchItemViewModel(
            missionName = launchItemDomain.missionName,
            missionPatchSmall = launchItemDomain.missionPatchSmall,
            launchSuccess = launchItemDomain.launchSuccess,
            upcoming = launchItemDomain.upcoming ?: false,
            rocketName = launchItemDomain.rocketName,
            wikipedia = launchItemDomain.wikipedia,
            videoLink = launchItemDomain.videoLink,

            rocketType = launchItemDomain.rocketType,
            launchYear = launchItemDomain.launchYear,
            launchDateUtc = launchItemDomain.launchDateUtc,
            launchDateUnix = launchItemDomain.launchDateUnix ?: 0,
            resourceProvider = resourceProvider
        )
    }
