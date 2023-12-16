package com.example.populartvseries.domain.usecase

import com.example.populartvseries.domain.model.Role
import com.example.populartvseries.domain.model.Series
import com.example.populartvseries.domain.repository.SeriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSeriesDetailUseCase @Inject constructor(private val seriesRepository: SeriesRepository) {

    suspend fun getSeriesDetail(seriesId: Int): Flow<Series?> {
        return seriesRepository.getSeriesById(seriesId)
    }

    suspend fun getSeriesCast(seriesId: Int): Flow<List<Role?>> {
        return seriesRepository.getSeriesCast(seriesId)
    }

}