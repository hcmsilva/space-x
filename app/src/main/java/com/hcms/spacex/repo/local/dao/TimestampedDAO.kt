package com.hcms.spacex.repo.local.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import com.hcms.spacex.repo.local.domain.TimestampedEntity
import io.reactivex.Completable
import io.reactivex.Observable


abstract class TimestampedDAO<T : TimestampedEntity> {
    @Insert(onConflict = REPLACE)
    internal abstract fun save(data: T): Completable

    fun saveWithTimestamp(data: T, timestamp: Long = System.currentTimeMillis()) =
        save(data.apply {
            modifiedAt = timestamp
        })

    open fun saveWithTimestamp(
        data: List<T>,
        timestamp: Long = System.currentTimeMillis()
    ): Completable =
        Observable.fromIterable(data)
            .flatMapCompletable { launch -> saveWithTimestamp(launch, timestamp) }
}