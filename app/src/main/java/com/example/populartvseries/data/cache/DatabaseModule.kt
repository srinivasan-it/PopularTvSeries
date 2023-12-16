package com.example.populartvseries.data.cache

import android.content.Context
import androidx.room.Room
import com.example.populartvseries.data.cache.dao.CastDao
import com.example.populartvseries.data.cache.dao.SeriesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): SeriesDatabase {
        return Room.databaseBuilder(appContext, SeriesDatabase::class.java, "series").build()
    }

    @Singleton
    @Provides
    fun provideSeriesDao(seriesDatabase: SeriesDatabase): SeriesDao {
        return seriesDatabase.seriesDao()
    }

    @Singleton
    @Provides
    fun provideCastDao(seriesDatabase: SeriesDatabase): CastDao {
        return seriesDatabase.castDao()
    }
}