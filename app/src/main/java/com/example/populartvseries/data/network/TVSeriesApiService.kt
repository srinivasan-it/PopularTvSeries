package com.example.populartvseries.data.network

import com.example.populartvseries.data.network.model.CastDto
import com.example.populartvseries.data.network.model.SeriesDto
import com.example.populartvseries.data.network.model.SeriesPageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TVSeriesApiService {

    @GET(ApiConstants.GET_POPULAR)
    suspend fun getPopularSeries(
        @Query("page") page: Int
    ): SeriesPageDto

    @GET(ApiConstants.SERIES)
    suspend fun getDetails(@Path("id") tvId: Int): SeriesDto

    @GET(ApiConstants.CREDITS)
    suspend fun getCredits(@Path("id") id: Int): CastDto

}