package com.hcms.spacex.repo

import com.hcms.spacex.repo.local.domain.TimestampedEntity
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CacheValidatorTest {

    lateinit var timestampedEntity: TimestampedEntity
    lateinit var subject: CacheValidator

    @Before
    fun setup() {
        timestampedEntity = mockk()
        subject = CacheValidator()
    }

    @Test
    fun `Given non-stale cache entry When cacheIsStale invoked Then return false`() {
        every { timestampedEntity.modifiedAt } returns Long.MAX_VALUE

        assertFalse(subject.cacheIsStale(listOf(timestampedEntity)))
    }

    @Test
    fun `Given stale cache entry When cacheIsStale invoked Then return true`() {
        every { timestampedEntity.modifiedAt } returns 0L

        assertTrue(subject.cacheIsStale(listOf(timestampedEntity)))
    }
}