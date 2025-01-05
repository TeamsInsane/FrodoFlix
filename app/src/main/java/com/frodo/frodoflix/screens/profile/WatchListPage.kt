package com.frodo.frodoflix.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.frodo.frodoflix.api.TMDB
import com.frodo.frodoflix.data.Movie
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.viewmodels.SharedViewModel
import org.json.JSONObject
import kotlin.math.min

@Composable
fun DisplayWantToWatchPage(sharedViewModel: SharedViewModel) {
    val navController = sharedViewModel.navController ?: return

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            item {
                BackToPreviousScreen(navController)

                // Watchlist movies text
                Text(
                    text = "Watchlist movies",
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp),
                    fontWeight = FontWeight.Bold
                )

                DisplayMoviesColumnWTW(sharedViewModel)
            }
        }

        BottomMenuBar(navController)
    }
}

@Composable
fun DisplayMoviesColumnWTW(sharedViewModel: SharedViewModel) {
    val wantToWatchList = sharedViewModel.watchlist.collectAsState().value

    for (i in 0..wantToWatchList.size / 3) {
        val wantToWatchMovieSublist =
            wantToWatchList.subList(i * 3, min(i * 3 + 3, wantToWatchList.size))


        DisplayListMoviesRow(wantToWatchMovieSublist, sharedViewModel)
    }
}

@Composable
fun DisplayListMoviesRow(movieIDList: List<Int>, sharedViewModel: SharedViewModel) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start
    ){
        for (id in movieIDList) {
            var data by remember { mutableStateOf<JSONObject?>(null) }

            LaunchedEffect(true) {
                data = TMDB.getDataFromTMDB(
                    "https://api.themoviedb.org/3/movie/${id}?language=en-US",
                    ""
                ) as? JSONObject
            }

            if (data == null) {
                Log.d("movie", "TUDDNULL")
                return
            }

            val nonNullData = data as JSONObject

            val title = nonNullData.getString("title")
            val overview = nonNullData.getString("overview")
            val posterUrl = nonNullData.getString("poster_path")

            val movie = Movie(id, title, overview, posterUrl)
            DisplayMovie(movie, sharedViewModel)
        }
    }
}


@Composable
fun DisplayMovie(movie: Movie, sharedViewModel: SharedViewModel) {
    Log.d("size", LocalConfiguration.current.screenWidthDp.toString())
    Column (
        modifier = Modifier
            .width((LocalConfiguration.current.screenWidthDp / 3 - 8).dp)
            .padding(8.dp)
            .padding(bottom = 0.dp)
            .clickable {
                sharedViewModel.selectedMovie = movie
                sharedViewModel.navController?.navigate("movie_page")
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .width((LocalConfiguration.current.screenWidthDp / 3 - 2 * 8).dp)
                .height(180.dp)
        ) {
            // Loading states for images (loading image before the image is loaded...)
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500/${movie.posterUrl}")
                    .crossfade(true)
                    .build(),
                contentDescription = "$movie.title poster",
                modifier = Modifier.fillMaxWidth()
            ) {
                // Progress indicator
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    is AsyncImagePainter.State.Success -> {
                        SubcomposeAsyncImageContent()
                    }

                    else -> {}
                }
            }
        }

        //Movie title
        Text(
            text = movie.title,

            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}