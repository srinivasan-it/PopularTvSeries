package com.example.populartvseries.ui.screen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.populartvseries.ui.screen.dashboard.DashboardScreen
import com.example.populartvseries.ui.screen.dashboard.SeriesListViewModel
import com.example.populartvseries.ui.screen.detail.DetailScreen
import com.example.populartvseries.ui.screen.detail.SeriesViewModel

@Composable
fun TVSeriesApp() {
    val navController = rememberNavController()
    val listViewModel: SeriesListViewModel = viewModel()
    val seriesDetailViewModel: SeriesViewModel = viewModel()
    NavHost(navController = navController, startDestination = TVSeriesAppScreen.MainScreen.route) {
        composable(route = TVSeriesAppScreen.MainScreen.route) {
            DashboardScreen(navController = navController, viewModel = listViewModel)
        }
        composable(
            route = TVSeriesAppScreen.DetailScreen.route + "/{s_id}",
            arguments = listOf(
                navArgument("s_id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { entry ->
            DetailScreen(
                entry.arguments?.getInt("s_id"),
                navController = navController,
                viewModel = seriesDetailViewModel
            )
        }
    }
}

