package com.example.populartvseries.data.cache

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.populartvseries.data.cache.dao.SeriesDao
import com.example.populartvseries.domain.model.Series
import javax.inject.Inject


class SeriesPagingSource @Inject constructor(private val seriesDao: SeriesDao) :
    PagingSource<Int, Series>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Series> {
        return LoadResult.Page(
            data = emptyList(),
            prevKey = 0,
            nextKey = 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Series>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}