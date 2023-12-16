package com.example.populartvseries.data.network.model

import com.example.populartvseries.domain.model.Series
import com.google.gson.annotations.SerializedName

data class SeriesDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("original_name") val originalName: String,
    @SerializedName("poster_path") val poster: String,
    @SerializedName("backdrop_path") val backdrop: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("vote_average") val rating: String,
    @SerializedName("first_air_date") val releaseDate: String,
    @SerializedName("tagline") val tagline: String?,
    @SerializedName("number_of_episodes") val noOfEpisodes: Int?,
    @SerializedName("number_of_seasons") val noOfSeasons: Int?,
    @SerializedName("created_by") val createdByList: List<CreatedBy>?,
    @SerializedName("genres") val genresList: List<Genre>?
) {
    fun toDomain(page: Int): Series {
        val createdBy = createdByList?.joinToString(separator = ",") { it.name }
        val genres = genresList?.joinToString(separator = ",") { it.name }
        return Series(
            id = id,
            name = name,
            originalName = originalName,
            poster = poster,
            backdrop = backdrop,
            overview = overview,
            tagline = tagline,
            releaseDate = releaseDate,
            page = page,
            noOfEpisodes = noOfEpisodes,
            noOfSeasons = noOfSeasons,
            createdBy = createdBy,
            genres = genres
        )
    }

}

data class CreatedBy(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class Genre(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)