package com.example.populartvseries.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.populartvseries.domain.model.Role
import com.example.populartvseries.domain.model.Series
import com.example.populartvseries.domain.usecase.GetSeriesDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesViewModel @Inject constructor(private val getSeriesUseCase: GetSeriesDetailUseCase) :
    ViewModel() {

    var series: Flow<Series?> = emptyFlow()
    var cast: Flow<List<Role?>>


    init {
        series = getSeriesById(null)
        cast = getCast(null)
    }

    /**
     * Gets the full data of a series
     */
    fun getSeriesById(seriesId: Int?): Flow<Series?> {
        if (seriesId != null) {
            viewModelScope.launch {
                series = getSeriesUseCase.getSeriesDetail(seriesId)
                    .distinctUntilChanged()
                    .map {
                        it
                    }
            }
        }
        return series
    }

    fun getCast(seriesId: Int?): Flow<List<Role?>> {
        if (seriesId != null) {
            viewModelScope.launch {
                cast = getSeriesUseCase.getSeriesCast(seriesId)
            }
        }
        return cast
    }
}

