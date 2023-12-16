package com.example.populartvseries.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.populartvseries.domain.model.Series
import com.example.populartvseries.domain.usecase.GetSeriesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesListViewModel @Inject constructor(private val getSeriesListUseCase: GetSeriesListUseCase) :
    ViewModel() {

    var seriesList: Flow<PagingData<Series>>

    init {
        seriesList = loadSeries()
    }

    /**
     * Gets a list of popular series
     */
    private fun loadSeries(): Flow<PagingData<Series>> {
        var seriesListFromRepo: Flow<PagingData<Series>> = emptyFlow()
        viewModelScope.launch {
            seriesListFromRepo = getSeriesListUseCase.getMostPopularSeries()
                .cachedIn(viewModelScope)
        }
        return seriesListFromRepo
    }

    fun reloadSeries() {
        viewModelScope.launch {
            getSeriesListUseCase.reloadSeries()
        }
    }

    private var seriesListScrollState = 0
    fun saveSeriesListScrollState(scrollIndex: Int) {
        seriesListScrollState = scrollIndex
    }

    fun getSeriesListScrollState(): Int {
        return seriesListScrollState
    }
}

