package com.example.populartvseries.data.cache.model

import com.example.populartvseries.domain.model.Series

data class CachedSeriesAggregate(
    val series_id: Int,
    val name: String?,
    val original_name: String?,
    var poster: String?,
    var backdrop: String?,
    val overview: String?,
    val tagline: String?,
    val release_date: String?,
    val page: Int = 0,
    val no_of_episodes: Int?,
    val no_of_seasons: Int?,
    val created_by: String?,
    val genres: String?
) {
    fun toDomain() =
        Series(
            id = series_id,
            name = name,
            originalName = original_name,
            poster = poster,
            backdrop = backdrop,
            overview = overview,
            releaseDate = release_date,
            page = page,
            tagline = tagline,
            noOfEpisodes = no_of_episodes,
            noOfSeasons = no_of_seasons,
            createdBy = created_by,
            genres = genres
        )
}