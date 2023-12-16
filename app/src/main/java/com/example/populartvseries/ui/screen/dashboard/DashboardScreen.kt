package com.example.populartvseries.ui.screen.dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.example.populartvseries.R
import com.example.populartvseries.data.network.ApiConstants
import com.example.populartvseries.domain.model.Series
import com.example.populartvseries.ui.composable.EmptyListWarning
import com.example.populartvseries.ui.composable.LoadingIndicator
import com.example.populartvseries.ui.screen.TVSeriesAppScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class ListType {
    Popular
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardScreen(navController: NavController, viewModel: SeriesListViewModel) {

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    // state
    val seriesList: LazyPagingItems<Series> =
        viewModel.seriesList.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collectAsLazyPagingItems()

    var listDisplayed by rememberSaveable { mutableStateOf(ListType.Popular) }

    val scaffoldState = rememberScaffoldState()
    val snackbarCoroutineScope = rememberCoroutineScope()

    val pullRefreshState =
        rememberPullRefreshState(refreshing = true, onRefresh = { viewModel.reloadSeries() })

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            PopularSeriesTopAppBar(
                listDisplayed,
                onFavoritesActionClicked = { listDisplayed = it },
                scaffoldState,
                snackbarCoroutineScope
            )
        },
    ) { innerPadding ->
        BodyContent(
            Modifier.padding(innerPadding),
            listDisplayed,
            seriesList,
            navController,
            viewModel,
            pullRefreshState
        ) { viewModel.reloadSeries() }
    }
}

@Composable
fun PopularSeriesTopAppBar(
    listDisplayed: ListType,
    onFavoritesActionClicked: (listDisplayed: ListType) -> Unit,
    scaffoldState: ScaffoldState, snackbarCoroutineScope: CoroutineScope
) {

    TopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        actions = {
            IconButton(onClick = {
                snackbarCoroutineScope.launch {
//                    scaffoldState.snackbarHostState.showSnackbar("")
                }
            }) {
                Icon(Icons.Filled.Search, contentDescription = "Search series")
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BodyContent(
    modifier: Modifier,
    listDisplayed: ListType,
    seriesPosterList: LazyPagingItems<Series>,
    navController: NavController,
    viewModel: SeriesListViewModel,
    pullRefreshState: PullRefreshState,
    reload: () -> Unit
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colors.background)
            .fillMaxHeight()
            .pullRefresh(pullRefreshState)
    ) {

        if (seriesPosterList.itemCount == 0) {
            EmptyListWarning(
                modifier
                    .fillMaxSize()
                    .padding(32.dp)
                    .align(Alignment.Center),
                message = stringResource(R.string.no_series_found),
                actionText = stringResource(R.string.reload),
                onButtonClick = { reload() })
        } else {
            val scrollingListPosition = viewModel.getSeriesListScrollState()
            PosterGrid(
                seriesPosterList,
                scrollingListPosition,
                onPosterClick = { seriesId, scrollId ->
                    navController.navigate(TVSeriesAppScreen.DetailScreen.withArgs(seriesId))
                    viewModel.saveSeriesListScrollState(scrollId)
                }
            )
//                    PullRefreshIndicator(refreshing = false, state = pullRefreshState)
        }

    }

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalCoilApi::class)
@Composable
fun PosterGrid(
    series: LazyPagingItems<Series>,
    scrollingListPosition: Int,
    onPosterClick: (Int, Int) -> Unit
) {
    val savedListState = rememberLazyGridState(scrollingListPosition)
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = savedListState,
        content = {
            items(series.itemCount) { index ->
                series[index]?.let {
                    PosterItem(
                        ApiConstants.BASE_IMAGE_URL + it.poster,
                        it.name,
                        it.id,
                        savedListState.firstVisibleItemIndex,
                        onPosterClick
                    )
                }
            }
            series.apply {
                when {
                    loadState.refresh is
                            LoadState.Loading -> {
                        item { LoadingIndicator() }
                        item { LoadingIndicator() }
                    }

                    loadState.append is
                            LoadState.Loading -> {
                        item { LoadingIndicator() }
                        item { LoadingIndicator() }
                    }

                    loadState.refresh is
                            LoadState.Error -> {
                    }

                    loadState.append is
                            LoadState.Error -> {
                    }
                }
            }
        }
    )
}

@ExperimentalCoilApi
@Composable
fun PosterItem(
    poster: String?,
    title: String?,
    seriesId: Int,
    scrollId: Int,
    onPosterClick: (Int, Int) -> Unit
) {
    val posterPainter = rememberImagePainter(
        data = poster,
        builder = { size(OriginalSize) })
    Box(
        Modifier
            .border(width = 1.dp, color = MaterialTheme.colors.secondaryVariant)
            .height(278.dp)
            .width(185.dp)
    ) {
        Image(
            painter = posterPainter,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    onPosterClick(seriesId, scrollId)
                })
        )
    }
    val imageState = posterPainter.state
    if (imageState is ImagePainter.State.Loading) {
        Box(
            Modifier
                .height(278.dp)
                .width(185.dp),
            Alignment.Center
        ) {
            LoadingIndicator()
        }
    }
    if (imageState is ImagePainter.State.Error) {
        Box(
            Modifier
                .height(278.dp)
                .width(185.dp)
                .border(
                    1.dp, MaterialTheme.colors.onSecondary.copy(alpha = 0.1F)
                ),
            Alignment.Center
        ) {
            Icon(
                Icons.Default.MovieCreation,
                title,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.Center),
                tint = Color.Black.copy(alpha = 0.2F)
            )
            if (title != null) {
                Text(
                    title,
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}

