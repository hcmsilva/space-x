package com.hcms.spacex.repo.di

import com.hcms.spacex.repo.IRepo
import com.hcms.spacex.repo.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun provideRepo(repoImpl: Repository): IRepo
}