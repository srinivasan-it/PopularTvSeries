package com.example.populartvseries.ui.screen

sealed class TVSeriesAppScreen(val route: String) {
    object MainScreen : TVSeriesAppScreen("dashboard_screen")
    object DetailScreen : TVSeriesAppScreen("detail_screen")

    fun withArgs(vararg args: Int): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
