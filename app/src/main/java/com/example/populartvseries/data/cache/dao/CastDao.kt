package com.example.populartvseries.data.cache.dao

import androidx.room.*
import com.example.populartvseries.data.cache.model.CachedArtist
import com.example.populartvseries.data.cache.model.CachedRole
import com.example.populartvseries.data.cache.model.CachedRoleAggregate
import kotlinx.coroutines.flow.Flow

@Dao
interface CastDao {

    @Transaction
    @Query("SELECT * FROM role WHERE series_id= :seriesId")
    fun getSeriesCast(seriesId: Int): Flow<List<CachedRoleAggregate?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(artist: List<CachedArtist>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoles(role: List<CachedRole>)
}