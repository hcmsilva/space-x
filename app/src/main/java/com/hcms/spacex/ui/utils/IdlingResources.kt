package com.hcms.spacex.ui.utils

import androidx.test.espresso.idling.CountingIdlingResource

object CountingIdlingResourceSingleton {

    private const val RESOURCE_COMPANY_INFO = "RESOURCE_COMPANY_INFO"
    private const val RESOURCE_ALL_LAUNCHES = "RESOURCE_ALL_LAUNCHES"

    @JvmField
    val countingIdlingResCompanyInfo = CountingIdlingResource(RESOURCE_COMPANY_INFO)
    @JvmField
    val countingIdlingResAllLaunches = CountingIdlingResource(RESOURCE_ALL_LAUNCHES)

    fun incrementCompanyInfo() {
        countingIdlingResCompanyInfo.increment()
    }

    fun decrementCompanyInfo() {
        if (!countingIdlingResCompanyInfo.isIdleNow) {
            countingIdlingResCompanyInfo.decrement()
        }
    }

    fun incrementAllLaunches() {
        countingIdlingResAllLaunches.increment()
    }

    fun decrementAllLaunches() {
        if (!countingIdlingResAllLaunches.isIdleNow) {
            countingIdlingResAllLaunches.decrement()
        }
    }
}