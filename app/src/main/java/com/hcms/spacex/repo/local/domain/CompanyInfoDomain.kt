package com.hcms.spacex.repo.local.domain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companies")
data class CompanyInfoDomain(
    @PrimaryKey val name: String,
    val summary: String? = null,
    val coo: String? = null,
    val founder: String? = null,
    val founded: Int? = null,
    val vehicles: Int? = null,
    val ceo: String? = null,
    val launchSites: Int? = null,
    @Embedded
    val headquarters: HeadquartersDomain? = null,
    val valuation: Long? = null,
    val employees: Int? = null,
    val testSites: Int? = null,
    val cto: String? = null,
    val ctoPropulsion: String? = null,
    override var modifiedAt: Long
) : TimestampedEntity()

data class HeadquartersDomain(
    val address: String? = null,
    val city: String? = null,
    val state: String? = null
)