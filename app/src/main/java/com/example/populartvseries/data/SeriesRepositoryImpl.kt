package com.example.populartvseries.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import com.example.populartvseries.data.network.SeriesListRemoteMediator
import com.example.populartvseries.domain.model.Role
import com.example.populartvseries.domain.model.Series
import com.example.populartvseries.domain.repository.DataRefreshManager
import com.example.populartvseries.domain.repository.SeriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Combines local and remote data sources to return series data
 * Source of truth is the DB cache data
 */
class SeriesRepositoryImpl @Inject constructor(
    @LocalDataSourceModule
    private val seriesLocalDataSource: SeriesDataSource,
    @NetworkDataSourceModule
    private val seriesNetworkDataSource: SeriesDataSource,
    dataRefreshManagerImpl: DataRefreshManager
) : SeriesRepository {

    @ExperimentalPagingApi
    val seriesRemoteMediator = SeriesListRemoteMediator(
        seriesLocalDataSource,
        seriesNetworkDataSource,
        dataRefreshManagerImpl
    )


    @ExperimentalPagingApi
    override suspend fun getMostPopularSeries(): Flow<PagingData<Series>> {

        // return the paging source
        val mostPopularSeries =
            seriesLocalDataSource.getPagedSeries(seriesRemoteMediator).map { pagingData ->
                pagingData.map {
                    it.toDomain()
                }
            }
        return mostPopularSeries
    }

    @ExperimentalPagingApi
    override suspend fun reloadSeries() {
        seriesRemoteMediator.reload()
    }

    override suspend fun getSeriesCast(seriesId: Int): Flow<List<Role?>> {
        val seriesCast = seriesLocalDataSource.getSeriesCast(seriesId).map {
            when {
                it.isEmpty() -> {
                    insertCastToCache(seriesId)
                    it
                }
                else -> {
                    it
                }
            }
        }
        return seriesCast
    }

    private suspend fun insertCastToCache(seriesId: Int) {
        // get cast list from network data source
        val castList: List<Role?> = seriesLocalDataSource.getFreshSeriesCast(seriesId)
        seriesLocalDataSource.insertCast(castList)
    }

    override suspend fun getSeriesById(seriesId: Int): Flow<Series?> {
        val fullSeriesDataFlow = seriesLocalDataSource.getSeriesById(seriesId)
            .distinctUntilChanged()
            .map {
                insertFreshSeriesDataToCache(seriesId)
                it
            }
        return fullSeriesDataFlow
    }

    private suspend fun insertFreshSeriesDataToCache(seriesId: Int) {
        val fullSeriesData = seriesNetworkDataSource.getSeriesById(seriesId)
        seriesLocalDataSource.insertSeries(fullSeriesData)
    }

}