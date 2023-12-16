package com.example.populartvseries.domain.model

import com.example.populartvseries.data.cache.model.CachedSeries

data class Series(
    val id: Int,
    val name: String?,
    val originalName: String?,
    val poster: String?,
    val backdrop: String?,
    val overview: String?,
    val tagline: String?,
    val releaseDate: String?,
    val page: Int,
    val noOfEpisodes: Int?,
    val noOfSeasons: Int?,
    val createdBy: String?,
    val genres: String?
) {
    fun toCache() =
        CachedSeries(
            series_id = id,
            name = name,
            original_name = originalName,
            poster = poster,
            backdrop = backdrop,
            overview = overview,
            release_date = releaseDate,
            page = page,
            tagline = tagline,
            no_of_episodes = noOfEpisodes,
            no_of_seasons = noOfSeasons,
            created_by = createdBy,
            genres = genres
        )
}