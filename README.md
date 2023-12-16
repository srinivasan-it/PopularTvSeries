# PopularTvSeries

# This app provides popular TV shows, those data are fetching from https://www.themoviedb.org/ [TMDB]

# Fetches the data from the remote and stores in local then showing it in UI

# Used MVVM pattern with Clean Architecture

- Written in Kotlin
- Used Jetpack compose
- Material
- Retrofit for network call
- Room for local database
- Coil for image rendering
- TMDB for getting the data from remote
- Used paging 3
- Dagger for dependency injection

# To use this app
- No authentication required
- Popular TV Shows are listed in the dashboard
- By click on any show will redirect to show detail screen
- Once data fetched from remote, then the app can be used in offline mode where already fetched data will be loaded
