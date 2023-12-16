package com.example.populartvseries.domain.usecase

import androidx.paging.PagingData
import com.example.populartvseries.domain.model.Series
import com.example.populartvseries.domain.repository.SeriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSeriesListUseCase @Inject constructor(private val seriesRepository: SeriesRepository) {

    suspend fun getMostPopularSeries(): Flow<PagingData<Series>> {
        return seriesRepository.getMostPopularSeries()
    }

    suspend fun reloadSeries() {
        seriesRepository.reloadSeries()
    }

}