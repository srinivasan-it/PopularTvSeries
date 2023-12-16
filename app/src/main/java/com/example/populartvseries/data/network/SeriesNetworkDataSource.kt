package com.example.populartvseries.data.network

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.populartvseries.data.SeriesDataSource
import com.example.populartvseries.data.cache.model.CachedSeriesAggregate
import com.example.populartvseries.domain.model.Role
import com.example.populartvseries.domain.model.Series
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Manages data in and out the API
 */
class SeriesNetworkDataSource @Inject constructor(
    private val apiService: TVSeriesApiService,
    private val context: Context
) : SeriesDataSource {


    /**
     * Load pages of most popular series from the API
     */
    override suspend fun getFreshPopularSeries(page: Int): List<Series> {
        return if (NetworkConnectivityManager.isNetworkConnected(context)) {
            val popularSeries: List<Series> =
                apiService.getPopularSeries(page).seriesList.map {
                    it.toDomain(page)
                }
            popularSeries
        } else {
            emptyList()
        }
    }

    override suspend fun getSeriesById(seriesId: Int): Flow<Series?> {
        return if (NetworkConnectivityManager.isNetworkConnected(context)) {
            val seriesData = apiService.getDetails(seriesId)
            flow { emit(seriesData.toDomain(0)) }
        } else {
            emptyFlow()
        }
    }


    @ExperimentalPagingApi
    override suspend fun getPagedSeries(seriesRemoteMediator: SeriesListRemoteMediator): Flow<PagingData<CachedSeriesAggregate>> {
        return emptyFlow()
    }


    override suspend fun refreshSeries(popularSeries: List<Series>) {
    }


    override suspend fun insertSeries(fullSeriesData: Flow<Series?>) {
    }

    /**
     * Load 10 artist roles for this series from the API
     */
    override suspend fun getFreshSeriesCast(seriesId: Int): List<Role?> {
        return if (NetworkConnectivityManager.isNetworkConnected(context)) {
            val cast = apiService.getCredits(seriesId).roleList.take(10).map {
                it.toDomain(seriesId)
            }
            cast
        } else {
            emptyList()
        }
    }

    override suspend fun getSeriesCast(seriesId: Int): Flow<List<Role?>> {
        return emptyFlow()
    }

    override suspend fun insertCast(roleList: List<Role?>) {
    }


    override suspend fun addFreshPopularSeries(series: List<Series>) {
    }

    override suspend fun countSeries(): Int {
        return 0
    }


}