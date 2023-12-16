package com.example.populartvseries.data

import com.example.populartvseries.data.cache.SeriesLocalDataSource
import com.example.populartvseries.data.network.SeriesNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class LocalDataSourceModule

@Qualifier
annotation class NetworkDataSourceModule

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

    @Singleton
    @Binds
    @LocalDataSourceModule
    abstract fun bindSeriesLocalDataSource(impl: SeriesLocalDataSource): SeriesDataSource

    @Singleton
    @Binds
    @NetworkDataSourceModule
    abstract fun bindSeriesNetworkDataSource(impl: SeriesNetworkDataSource): SeriesDataSource
}