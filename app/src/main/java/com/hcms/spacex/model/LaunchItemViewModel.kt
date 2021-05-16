package com.hcms.spacex.model

import com.hcms.spacex.R
import com.hcms.spacex.ui.utils.ResourceProvider
import java.util.concurrent.TimeUnit

data class LaunchItemViewModel(
    val missionName: String = "",
    val missionPatchSmall: String? = null,
    val launchSuccess: Boolean? = null,
    val upcoming: Boolean,
    val wikipedia: String? = null,
    val videoLink: String? = null,

    private val launchYear: String? = null,
    private val launchDateUtc: String? = null,
    private val launchDateUnix: Long,
    private val rocketType: String? = null,
    private val rocketName: String? = null,
    private val resourceProvider: ResourceProvider
) {
    val dayAtTime: String = getDateAtTime()
    val rocketNameType: String = getRckNameType()
    val daysSinceFromLaunch: String = getDaysSLaunch()
    val todayMinusLaunchDateInDays: String = getTodayLaunchDiff().toString()

    private fun getDateAtTime() = String.format(
        resourceProvider.getString(R.string.dateAtTime),
        getDateFromUtc(),
        getTimeFromUtc()
    )

    private fun getDateFromUtc() = launchDateUtc?.split("T")?.first() ?: "--"
    private fun getTimeFromUtc() = launchDateUtc?.split("T")?.get(1)?.take(8) ?: "--"

    private fun getRckNameType() = "${rocketName}/${rocketType}"
    private fun getTodayLaunchDiff() =
        TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - (launchDateUnix * 1000))

    private fun getDaysSLaunch(): String {
        val daysSFResource = when (upcoming) {
            true -> R.string.fromDays
            false -> R.string.sinceDays
        }
        val daysSFString = resourceProvider.getString(daysSFResource)
        return String.format(resourceProvider.getString(R.string.daysSinceFromLaunch), daysSFString)
    }

    fun isVisualRepresentationTheSame(other: LaunchItemViewModel): Boolean =
        missionName == other.missionName &&
                dayAtTime == other.dayAtTime &&
                rocketNameType == other.rocketNameType &&
                daysSinceFromLaunch == other.daysSinceFromLaunch &&
                todayMinusLaunchDateInDays == other.todayMinusLaunchDateInDays &&
                missionPatchSmall == other.missionPatchSmall &&
                launchSuccess == other.launchSuccess &&
                upcoming == other.upcoming
}
