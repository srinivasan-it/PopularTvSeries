package com.example.populartvseries.data.cache.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.populartvseries.data.cache.model.CachedSeries
import com.example.populartvseries.data.cache.model.CachedSeriesAggregate
import kotlinx.coroutines.flow.Flow

@Dao
interface SeriesDao {

    @Query("SELECT * FROM series")
    fun getMostPopularSeries(): Flow<List<CachedSeriesAggregate>>

    @Query("SELECT * FROM series ORDER BY page ASC")
    fun getSeriesPagingSource(): PagingSource<Int, CachedSeriesAggregate>

    @Transaction
    @Query("SELECT * FROM series WHERE series_id= :seriesId")
    fun getSeriesDetails(seriesId: Int): Flow<CachedSeriesAggregate?>

    @Transaction
    suspend fun refreshSeries(series: List<CachedSeries>): List<Long> {
        return insertNewSeries(series)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSeries(series: CachedSeries)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewSeries(series: List<CachedSeries>): List<Long>


    @Query("SELECT COUNT(series_id) FROM  series")
    suspend fun countSeries(): Int

}