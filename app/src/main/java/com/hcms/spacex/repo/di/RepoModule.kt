package com.hcms.spacex.repo.di

import com.hcms.spacex.repo.ICompanyInfoRepo
import com.hcms.spacex.repo.ILaunchesRepo
import com.hcms.spacex.repo.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun provideCompanyInfoRepo(repoImpl: Repository): ICompanyInfoRepo

    @Binds
    abstract fun provideLaunchesRepo(repoImpl: Repository): ILaunchesRepo
}