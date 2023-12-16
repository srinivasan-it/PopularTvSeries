package com.example.populartvseries.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.populartvseries.data.cache.dao.CastDao
import com.example.populartvseries.data.cache.dao.SeriesDao
import com.example.populartvseries.data.cache.model.CachedArtist
import com.example.populartvseries.data.cache.model.CachedRole
import com.example.populartvseries.data.cache.model.CachedSeries

@Database(
    entities = [
        CachedSeries::class,
        CachedArtist::class,
        CachedRole::class],
    version = 1,
    exportSchema = false
)
abstract class SeriesDatabase : RoomDatabase() {
    abstract fun seriesDao(): SeriesDao
    abstract fun castDao(): CastDao
}