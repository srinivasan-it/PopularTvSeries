package com.example.populartvseries.data.cache

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.populartvseries.data.SeriesDataSource
import com.example.populartvseries.data.cache.dao.CastDao
import com.example.populartvseries.data.cache.dao.SeriesDao
import com.example.populartvseries.data.cache.model.CachedArtist
import com.example.populartvseries.data.cache.model.CachedRole
import com.example.populartvseries.data.cache.model.CachedSeries
import com.example.populartvseries.data.cache.model.CachedSeriesAggregate
import com.example.populartvseries.data.network.ApiConstants
import com.example.populartvseries.data.network.SeriesListRemoteMediator
import com.example.populartvseries.domain.model.Role
import com.example.populartvseries.domain.model.Series
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Manages data in and out the DB (local cache = source of truth for this app)
 */
class SeriesLocalDataSource @Inject constructor(
    private val seriesDao: SeriesDao,
    private val castDao: CastDao,
) : SeriesDataSource {


    @ExperimentalPagingApi
    override suspend fun getPagedSeries(seriesRemoteMediator: SeriesListRemoteMediator): Flow<PagingData<CachedSeriesAggregate>> {
        // setup the pager
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            remoteMediator = seriesRemoteMediator,
            pagingSourceFactory = {
                seriesDao.getSeriesPagingSource()
            }
        ).flow
    }

    override suspend fun getFreshPopularSeries(page: Int): List<Series> {
        return emptyList()
    }

    override suspend fun addFreshPopularSeries(series: List<Series>) {
        // for the DB
        val popularSeriesToDb = mutableListOf<CachedSeries>()

        // convert API data to DB format
        var cachedSeries: CachedSeries
        for (s in series) {
            cachedSeries = CachedSeries(
                s.id,
                s.name,
                s.originalName,
                s.poster,
                s.backdrop,
                s.overview,
                s.tagline,
                s.releaseDate,
                s.page,
                s.noOfEpisodes,
                s.noOfSeasons,
                s.createdBy,
                s.genres
            )
            popularSeriesToDb.add(cachedSeries)
        }
        seriesDao.insertNewSeries(popularSeriesToDb)
    }

    override suspend fun countSeries(): Int {
        return seriesDao.countSeries()
    }


    override suspend fun getSeriesById(seriesId: Int): Flow<Series?> {
        return seriesDao.getSeriesDetails(seriesId)
            .map {
                it?.toDomain()
            }
    }

    override suspend fun insertSeries(fullSeriesData: Flow<Series?>) {
        fullSeriesData.map {
            if (it !== null) {
                seriesDao.insertSeries(it.toCache())
            }
        }
    }

    override suspend fun getFreshSeriesCast(seriesId: Int): List<Role?> {
        return emptyList()
    }

    override suspend fun getSeriesCast(seriesId: Int): Flow<List<Role?>> {
        return castDao.getSeriesCast(seriesId).map { castList ->
            castList.map {
                it?.artist?.image =
                    ApiConstants.BASE_IMAGE_URL + it?.artist?.image
                it?.toDomain()
            }
        }
    }

    override suspend fun insertCast(roleList: List<Role?>) {
        // for the DB
        val rolesToDb = mutableListOf<CachedRole>()
        val artistsToDb = mutableListOf<CachedArtist>()

        // convert API data to DB format
        var cachedRole: CachedRole
        var cachedArtist: CachedArtist
        for (role in roleList) {
            if (role != null) {
                cachedRole = CachedRole(
                    0,
                    role.artist_id,
                    role.series_id,
                    role.character
                )
                cachedArtist = CachedArtist(
                    0,
                    role.artist_id,
                    role.name,
                    role.image_path
                )
                rolesToDb.add(cachedRole)
                artistsToDb.add(cachedArtist)
            }
        }

        // insert into DB
        castDao.insertArtists(artistsToDb)
        castDao.insertRoles(rolesToDb)
    }


    /**
     * Refresh popular series in local data source
     */
    override suspend fun refreshSeries(popularSeries: List<Series>) {
        // for the DB
        val popularSeriesToDb = mutableListOf<CachedSeries>()

        // convert API data to DB format
        var cachedSeries: CachedSeries
        for (s in popularSeries) {
            cachedSeries = CachedSeries(
                s.id,
                s.name,
                s.originalName,
                s.poster,
                s.backdrop,
                s.overview,
                s.tagline,
                s.releaseDate,
                s.page,
                s.noOfEpisodes,
                s.noOfSeasons,
                s.createdBy,
                s.genres
            )
            popularSeriesToDb.add(cachedSeries)
        }
        seriesDao.refreshSeries(popularSeriesToDb)
    }



}