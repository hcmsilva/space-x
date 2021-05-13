package com.hcms.spacex.repo.remote.dto

import com.google.gson.annotations.SerializedName

data class CompanyInfoDTO(

    @field:SerializedName("summary")
    val summary: String? = null,

    @field:SerializedName("coo")
    val coo: String? = null,

    @field:SerializedName("founder")
    val founder: String? = null,

    @field:SerializedName("founded")
    val founded: Int? = null,

    @field:SerializedName("vehicles")
    val vehicles: Int? = null,

    @field:SerializedName("ceo")
    val ceo: String? = null,

    @field:SerializedName("launch_sites")
    val launchSites: Int? = null,

    @field:SerializedName("headquarters")
    val headquarters: Headquarters? = null,

    @field:SerializedName("valuation")
    val valuation: Long? = null,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("employees")
    val employees: Int? = null,

    @field:SerializedName("test_sites")
    val testSites: Int? = null,

    @field:SerializedName("cto")
    val cto: String? = null,

    @field:SerializedName("cto_propulsion")
    val ctoPropulsion: String? = null
)

data class Headquarters(

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("city")
    val city: String? = null,

    @field:SerializedName("state")
    val state: String? = null
)