package com.hcms.spacex.repo.local.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companies")
data class CompanyInfoDomain(
    @PrimaryKey val name: String,
    val founder: String? = null,
    val founded: Int? = null,
    val launchSites: Int? = null,
    val valuation: Long? = null,
    val employees: Int? = null,
    override var modifiedAt: Long
) : TimestampedEntity()
