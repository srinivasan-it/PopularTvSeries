package com.example.populartvseries.data.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.populartvseries.data.SeriesDataSource
import com.example.populartvseries.data.cache.model.CachedSeriesAggregate
import com.example.populartvseries.data.network.ApiConstants.ENDING_PAGE_INDEX
import com.example.populartvseries.data.network.ApiConstants.NUM_PAGES_IN_CACHE
import com.example.populartvseries.data.network.ApiConstants.NUM_RESULTS_PER_PAGE
import com.example.populartvseries.data.network.ApiConstants.STARTING_PAGE_INDEX
import com.example.populartvseries.domain.model.Series
import com.example.populartvseries.domain.repository.DataRefreshManager
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalPagingApi
class SeriesListRemoteMediator @Inject constructor(
    private val seriesLocalDataSource: SeriesDataSource,
    private val seriesNetworkDataSource: SeriesDataSource,
    private val dataRefreshManagerImpl: DataRefreshManager
) : RemoteMediator<Int, CachedSeriesAggregate>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CachedSeriesAggregate>
    ): MediatorResult {

        var pageRequested: Int? = getPageForLastItem(state)
        var loadSize = STARTING_PAGE_INDEX * NUM_RESULTS_PER_PAGE

        if (loadType == LoadType.APPEND) {
            pageRequested = pageRequested?.plus(1)
                ?: return MediatorResult.Success(endOfPaginationReached = pageRequested != null)
        } else if (loadType == LoadType.REFRESH && pageRequested == null) {
            val cachedSeriesCount = seriesLocalDataSource.countSeries()
            loadSize = NUM_PAGES_IN_CACHE * NUM_RESULTS_PER_PAGE
            pageRequested = if (cachedSeriesCount < loadSize) {
                1
            } else {
                // don't want to trigger API call
                null
            }
        } else {
            // don't want to trigger API call
            pageRequested = null
        }

        try {
            var endOfPaginationReached = false
            if (pageRequested != null) {
                // get more series from api service
                val seriesList: MutableList<Series> = mutableListOf()
                while (loadSize > 0) {
                    val seriesPage = seriesNetworkDataSource.getFreshPopularSeries(pageRequested)
                    seriesList.addAll(seriesPage)
                    pageRequested += 1
                    loadSize -= NUM_RESULTS_PER_PAGE
                }
                if (dataRefreshManagerImpl.checkIfRefreshNeeded()) {
                    // delete and add
                    seriesLocalDataSource.refreshSeries(seriesList)
                } else {
                    // only add
                    seriesLocalDataSource.addFreshPopularSeries(seriesList)
                }
                endOfPaginationReached = seriesList.isEmpty()
            }
            if (pageRequested == ENDING_PAGE_INDEX) {
                endOfPaginationReached = true
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

    }

    suspend fun reload() {
        val emptyPagingState = PagingState<Int, CachedSeriesAggregate>(
            emptyList(), null, PagingConfig(
                NUM_RESULTS_PER_PAGE
            ), 0
        )
        load(LoadType.REFRESH, emptyPagingState)
    }

    /**
     * Get the page of the last Series item loaded from the database
     * Returns null if no data passed to Mediator
     */
    private fun getPageForLastItem(state: PagingState<Int, CachedSeriesAggregate>): Int? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.page
    }


}