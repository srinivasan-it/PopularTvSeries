package com.example.populartvseries.data.network.model

import com.google.gson.annotations.SerializedName

data class SeriesPageDto(
    @SerializedName("page") val pageNumber: Int,
    @SerializedName("results") val seriesList: List<SeriesDto>
)