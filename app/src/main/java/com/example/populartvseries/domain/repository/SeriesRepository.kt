package com.example.populartvseries.domain.repository

import androidx.paging.PagingData
import com.example.populartvseries.data.SeriesRepositoryImpl
import com.example.populartvseries.domain.model.Role
import com.example.populartvseries.domain.model.Series
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

interface SeriesRepository {
    suspend fun getMostPopularSeries(): Flow<PagingData<Series>>
    suspend fun getSeriesById(seriesId: Int): Flow<Series?>
    suspend fun reloadSeries()
    suspend fun getSeriesCast(seriesId: Int): Flow<List<Role?>>
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DependenciesBindings {

    @Singleton
    @Binds
    abstract fun bindRepository(seriesRepositoryImpl: SeriesRepositoryImpl): SeriesRepository
}
