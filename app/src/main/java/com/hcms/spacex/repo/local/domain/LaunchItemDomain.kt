package com.hcms.spacex.repo.local.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "launches")
data class LaunchItemDomain(
    @PrimaryKey
    val missionName: String,
    val launchYear: String? = null,
    val launchDateUtc: String? = null,
    val launchDateUnix: Long? = null,
    val rocketType: String? = null,
    val rocketName: String? = null,
    val launchSuccess: Boolean? = null,
    val wikipedia: String? = null,
    val missionPatchSmall: String? = null,
    val videoLink: String? = null,
    val upcoming: Boolean? = null,
    override var modifiedAt: Long
) : TimestampedEntity()


