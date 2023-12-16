package com.example.populartvseries.ui.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.example.populartvseries.R
import com.example.populartvseries.data.network.ApiConstants
import com.example.populartvseries.domain.model.Role
import com.example.populartvseries.domain.model.Series
import com.example.populartvseries.ui.composable.LoadingIndicator
import com.example.populartvseries.ui.composable.ReleaseDate
import com.example.populartvseries.ui.theme.PopularTVSeriesTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun DetailScreen(seriesId: Int?, navController: NavController, viewModel: SeriesViewModel) {

    viewModel.getSeriesById(seriesId)
    val series: Series? by viewModel.series.collectAsState(null)

    viewModel.getCast(seriesId)
    val cast: List<Role?> by viewModel.cast.collectAsState(initial = emptyList())

    val scaffoldState = rememberScaffoldState()
    val snackbarCoroutineScope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            DetailTopAppBar(
                navController,
                scaffoldState,
                snackbarCoroutineScope
            )
        }) { innerPadding ->
        series?.let {
            DetailScreenBody(Modifier.padding(innerPadding), it, cast)
        }
    }
}


@Composable
fun DetailScreenBody(
    modifier: Modifier,
    series: Series,
    cast: List<Role?>,
) {
    val scrollState = rememberScrollState()
    PopularTVSeriesTheme {
        if (series == null) {
            LoadingIndicator()
        } else {
            Column(modifier = modifier.verticalScroll(scrollState)) {
                BackDrop(series)
                TitleRow(series)
                Text(
                    "${series.overview}",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
                if (cast.isNotEmpty()) {
                    CastList(cast)
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun BackDrop(series: Series) {
    val backDropPainter =
        rememberImagePainter(
            data = ApiConstants.BASE_IMAGE_URL + series.backdrop,
            builder = { size(OriginalSize) })
    Image(
        painter = backDropPainter,
        contentDescription = series.name,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
    )
    val imageState = backDropPainter.state
    if (imageState is ImagePainter.State.Loading) {
        Box(
            Modifier
                .height(280.dp)
                .fillMaxWidth(), Alignment.Center
        ) {
            LoadingIndicator()
        }
    }
    if (imageState is ImagePainter.State.Error) {
        Box(
            Modifier
                .height(280.dp)
                .fillMaxWidth(), Alignment.Center
        ) {
            Icon(
                Icons.Default.MovieCreation,
                series.name,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.Center),
                tint = Color.Black.copy(alpha = 0.2F)
            )
        }
    }
}

@Composable
fun TitleRow(series: Series) {
    Row(Modifier.background(MaterialTheme.colors.secondary)) {
        Column(
            Modifier
                .weight(6F, true)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${series.name}",
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(7.dp),
                color = MaterialTheme.colors.onSecondary,
                textAlign = TextAlign.Center
            )
            series.tagline?.let { tagline ->
                Text(
                    text = tagline,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .padding(7.dp),
                    color = MaterialTheme.colors.onSecondary,
                    textAlign = TextAlign.Center
                )
            }
            Row(Modifier.padding(top = 8.dp, bottom = 16.dp)) {
                Column(
                    Modifier.weight(1F),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ReleaseDate(series, MaterialTheme.colors.onSecondary.copy(alpha = 0.6f))
                }
            }
            series.noOfSeasons?.let { seasons ->
                series.noOfEpisodes?.let { episodes ->
                    Row(Modifier.padding(top = 8.dp, bottom = 16.dp)) {
                        Column(
                            Modifier.weight(1F),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "Seasons: $seasons",
                                modifier = Modifier
                                    .padding(8.dp),
                                color = MaterialTheme.colors.onSecondary
                            )
                            Text(
                                text = "Episodes: $episodes",
                                modifier = Modifier
                                    .padding(8.dp),
                                color = MaterialTheme.colors.onSecondary
                            )
                        }
                    }
                }
            }

        }
    }
}

@Preview(
    "Series Detail Screen Preview",
    heightDp = 1080
)
@Composable
private fun PreviewDetailScreenBody() {
    PopularTVSeriesTheme {
        DetailScreenBody(Modifier.padding(8.dp), testSeries, testCastList)
    }
}


@Composable
fun DetailTopAppBar(
    navController: NavController,
    scaffoldState: ScaffoldState,
    snackbarCoroutineScope: CoroutineScope
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back to list")
            }
        }
    )
}

@Composable
fun CastList(cast: List<Role?>) {
    Column {
        Text(
            stringResource(id = R.string.cast),
            Modifier.padding(top = 16.dp, bottom = 8.dp, start = 16.dp),
            style = MaterialTheme.typography.h2
        )
        LazyRow(Modifier.padding(start = 16.dp, top = 8.dp, bottom = 16.dp)) {
            items(cast){role ->
                role?.let {
                    CharacterBadge(it)
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CharacterBadge(role: Role) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(end = 16.dp), elevation = 10.dp
    ) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(154.dp)
                    .clip(CircleShape)
            ) {
                val painter = rememberImagePainter(data = role.image_path)
                Image(
                    painter = painter,
                    contentDescription = role.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                if (painter.state !is ImagePainter.State.Success) {
                    Icon(
                        Icons.Default.Face,
                        stringResource(R.string.cast),
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.Center),
                        tint = Color.Black.copy(alpha = 0.2F)
                    )
                }
            }
            Text(
                role.name.toString(),
                Modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.subtitle1
            )
            Text(role.character.toString(), style = MaterialTheme.typography.subtitle2)
        }
    }
}

val testSeries = Series(
    566525,
    "Shang-Chi and the Legend of the Ten Rings",
    "Shang-Chi and the Legend of the Ten Rings",
    "https://image.tmdb.org/t/p/w185/1BIoJGKbXjdFDAqUEiA2VHqkK1Z.jpg",
    "https://image.tmdb.org/t/p/w185/nDLylQOoIazGyYuWhk21Yww5FCb.jpg",
    "Shang-Chi must confront the past he thought he left behind when he is drawn into the web of the mysterious Ten Rings organization.",
    "Shang-Chi must confront the past he thought he left behind when he is drawn into the",
    "10.12.2023",
    1,
    10,
    5,
    "testTrailer",
    "Drama"
)


val testCastList = listOf(
    Role(
        1337,
        566525,
        "Tony Leung Chiu-wai",
        "https://image.tmdb.org/t/p/w154/nQbSQAws5BdakPEB5MtiqWVeaMV.jpg",
        "Xu Wenwu"
    ),
    Role(
        1625558,
        566525,
        "Awkwafina",
        "https://image.tmdb.org/t/p/w154/l5AKkg3H1QhMuXmTTmq1EyjyiRb.jpg",
        "Katy Chen"
    ),
    Role(
        2979464,
        566525,
        "Meng'er Zhang",
        "https://image.tmdb.org/t/p/w154/yMiYThzzkeVSsUI2sxh3iIWmMTy.jpg",
        "Xu Xialing"
    ),
    Role(
        123701,
        566525,
        "Fala Chen",
        "https://image.tmdb.org/t/p/w154/1eoEj3umXbxUkTXb5c7bmC3EUTh.jpg",
        "Ying Li"
    ),
    Role(
        1620,
        566525,
        "Michelle Yeoh",
        "https://image.tmdb.org/t/p/w154/wPI2wn6WJEtJr1oAMTLBLh92Ryc.jpg",
        "Ying Nan"
    ),
    Role(
        57609,
        566525,
        "Yuen Wah",
        "https://image.tmdb.org/t/p/w154/yMkMgs0tWlJXdTjYkiMcjvhYnRw.jpg",
        "Master Guang Bo"
    )
)