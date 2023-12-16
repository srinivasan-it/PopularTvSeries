package com.example.populartvseries.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.populartvseries.data.cache.model.CachedSeriesAggregate
import com.example.populartvseries.data.network.SeriesListRemoteMediator
import com.example.populartvseries.domain.model.Role
import com.example.populartvseries.domain.model.Series
import kotlinx.coroutines.flow.Flow

interface SeriesDataSource {

    @ExperimentalPagingApi
    suspend fun getPagedSeries(seriesRemoteMediator: SeriesListRemoteMediator): Flow<PagingData<CachedSeriesAggregate>>

    suspend fun refreshSeries(popularSeries: List<Series>)

    suspend fun getFreshPopularSeries(page: Int): List<Series>

    suspend fun addFreshPopularSeries(series: List<Series>)

    suspend fun countSeries(): Int

    suspend fun getSeriesById(seriesId: Int): Flow<Series?>

    suspend fun insertSeries(fullSeriesData: Flow<Series?>)

    suspend fun getFreshSeriesCast(seriesId: Int): List<Role?>

    suspend fun getSeriesCast(seriesId: Int): Flow<List<Role?>>

    suspend fun insertCast(roleList: List<Role?>)

}