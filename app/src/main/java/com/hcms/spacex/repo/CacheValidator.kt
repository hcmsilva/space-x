package com.hcms.spacex.repo

import com.hcms.spacex.BuildConfig
import com.hcms.spacex.repo.local.domain.TimestampedEntity
import javax.inject.Inject

class CacheValidator @Inject constructor() {
    fun <T : TimestampedEntity> cacheIsStale(localData: List<T>) =
        System.currentTimeMillis() - localData.first().modifiedAt > BuildConfig.CACHE_VALIDITY
}
