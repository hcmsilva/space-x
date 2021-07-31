package utils

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class IdlingRegistryRule(private val resourceList: List<CountingIdlingResource>) : TestRule {
    override fun apply(base: Statement, description: Description?): Statement =
        LaunchesIdlingRegistryStatement(base, resourceList)
}

class LaunchesIdlingRegistryStatement(
    private val base: Statement,
    private val resourceList: List<CountingIdlingResource>
) : Statement() {
    override fun evaluate() {
        //@Before - setup
        idlingRegistryListSetup(resourceList)
        try {
            base.evaluate()
        } finally {
            //@After - tearDown
            idlingRegistryListTearDown(resourceList)
        }
    }

    private fun idlingRegistryListTearDown(resourceList: List<CountingIdlingResource>) {
        resourceList.forEach {
            IdlingRegistry
                .getInstance()
                .unregister(it)
        }
    }

    private fun idlingRegistryListSetup(resourceList: List<CountingIdlingResource>) {
        resourceList.forEach {
            IdlingRegistry
                .getInstance()
                .register(it)
        }
    }
}