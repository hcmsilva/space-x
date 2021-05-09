package com.hcms.spacex.repo

import java.io.InputStreamReader

class MockFileReader(path: String) {
    val content: String

    init {
        val reader = InputStreamReader(this.javaClass.getResourceAsStream(path))
        content = reader.readText()
        reader.close()
    }
}