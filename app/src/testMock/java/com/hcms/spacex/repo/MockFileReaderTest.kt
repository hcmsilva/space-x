package com.hcms.spacex.repo

import org.junit.Assert.assertEquals
import org.junit.Test

class MockFileReaderTest {

    private val expectedTestFileContent="\"random content for test\""
    private val testFilePath="/dummy_content.json"

    @Test
    fun `given dummy json file when getContent called then content equals json file text`() {
        val subject = MockFileReader(testFilePath)

        assertEquals(expectedTestFileContent, subject.content)
    }
}