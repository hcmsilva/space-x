package utils

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.hcms.spacex.R
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.Is.`is`

fun nthChildOf(parentMatcher: Matcher<View?>, childPosition: Int): Matcher<View?> {
    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("with $childPosition child view of type parentMatcher")
        }

        override fun matchesSafely(view: View): Boolean {
            if (view.parent !is ViewGroup) {
                return parentMatcher.matches(view.parent)
            }
            val group = view.parent as ViewGroup
            return parentMatcher.matches(view.parent) && group.getChildAt(childPosition) == view
        }
    }
}

fun recyclerItemViewMatcher(childPosition: Int, itemMatcher: Matcher<View?>) =
    Espresso.onView(nthChildOf(ViewMatchers.withId(R.id.launches_list), childPosition))
        .check(ViewAssertions.matches(ViewMatchers.hasDescendant(itemMatcher)))

fun missionNameContentMatcher() = allOf(
    ViewMatchers.withId(R.id.missionNameTextView), ViewMatchers.withText("FalconSat"),
    ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.launches_list)))
)

fun dateAtTimeContentMatcher() = allOf(
    ViewMatchers.withId(R.id.dateAtTimeTextView), ViewMatchers.withText("2006-03-24 at 22:30:00"),
    ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.launches_list)))
)

fun missionSuccessContentMatcher() = allOf(
    ViewMatchers.withId(R.id.success_iv), ViewMatchers.withTagValue(`is`(false)),
    ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.launches_list)))
)

fun missionPatchContentMatcher() = allOf(
    ViewMatchers.withId(R.id.mission_patch_iv),
    ViewMatchers.withTagValue(`is`("https://images2.imgbox.com/3c/0e/T8iJcSN3_o.png"))
)

fun rocketNameTypeContentMatcher() = allOf(
    ViewMatchers.withId(R.id.nameTypeTextView), ViewMatchers.withText("Falcon 1/Merlin A"),
    ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.launches_list)))
)

fun daysSinceFromContentMatcher() = allOf(
    ViewMatchers.withId(R.id.nameTypeTextView), ViewMatchers.isDisplayed(),
    ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.launches_list)))
)

fun daysSinceFromLabelMatcher() = allOf(
    ViewMatchers.withId(R.id.rocketTextView),
    ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.launches_list)))
)

fun rocketLabelMatcher() = allOf(
    ViewMatchers.withId(R.id.rocketTextView), ViewMatchers.withText("Rocket:"),
    ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.launches_list)))
)

fun dateTimeLabelMatcher() = allOf(
    ViewMatchers.withId(R.id.dateTimeTextView), ViewMatchers.withText("Date/time:"),
    ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.launches_list)))
)

fun missionLabelMatcher() = allOf(
    ViewMatchers.withId(R.id.missionTextView), ViewMatchers.withText("Mission:"),
    ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.launches_list)))

)

fun companyDividerMatcher() = allOf(
    ViewMatchers.withId(R.id.company_info_divider), ViewMatchers.withText("COMPANY")
)

fun launchesDividerMatcher() = allOf(
    ViewMatchers.withId(R.id.launches_divider), ViewMatchers.withText("LAUNCHES")
)

fun companyInfoTextViewMatcher() = allOf(
    ViewMatchers.withId(R.id.company_info),
    ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(android.R.id.content))),
    ViewMatchers.isDisplayed()
)

fun spaceXMatcher() = allOf(
    ViewMatchers.withId(R.id.spacex_toolbar),
    ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(android.R.id.content))),
    ViewMatchers.isDisplayed()
)

fun spaceXTitleMatcher() = allOf(
    ViewMatchers.withId(R.id.title),
    ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.spacex_toolbar))),
    ViewMatchers.isDisplayed()
)

fun filterButtonMatcher() = allOf(
    ViewMatchers.withId(R.id.filter_button),
    ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.spacex_toolbar))),
    ViewMatchers.isDisplayed()
)