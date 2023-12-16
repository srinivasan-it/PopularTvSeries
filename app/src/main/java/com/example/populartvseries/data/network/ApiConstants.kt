package com.example.populartvseries.data.network

object ApiConstants {
    const val BASE_URL = "https://api.themoviedb.org/3/"

    // Get a list of the current popular series on TMDB. This list updates daily.
    const val GET_POPULAR = "tv/popular"

    // Get the information about a series.
    const val SERIES = "tv/{id}"

    const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w300"


    const val API_PARAM = "api_key"

    // API key for themovieDB.org
    const val API_KEY = "55a26a5ed9101c5b637b5e301a9975ed"

    const val CREDITS = "movie/{id}/credits"



    // related to paging
    const val STARTING_PAGE_INDEX = 1
    const val ENDING_PAGE_INDEX = 50
    const val NUM_PAGES_IN_CACHE = 3
    const val NUM_RESULTS_PER_PAGE = 20
}