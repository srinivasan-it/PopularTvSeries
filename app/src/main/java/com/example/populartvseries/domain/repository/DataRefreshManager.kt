package com.example.populartvseries.domain.repository

import com.example.populartvseries.data.DataRefreshManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

interface DataRefreshManager {
    fun checkIfRefreshNeeded(): Boolean
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataRefreshManagerBinding {

    @Singleton
    @Binds
    abstract fun bindDataRefreshManager(dataRefreshManagerIml: DataRefreshManagerImpl): DataRefreshManager
}