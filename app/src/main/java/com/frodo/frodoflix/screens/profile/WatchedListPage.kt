package com.frodo.frodoflix.screens.profile

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.frodo.frodoflix.api.TMDB
import com.frodo.frodoflix.data.Movie
import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.viewmodels.SharedViewModel
import org.json.JSONObject
import kotlin.math.min

@Composable
fun DisplayWatchedListPage(sharedViewModel: SharedViewModel) {
    val navController = sharedViewModel.navController ?: return

    DisplayMoviesColumn(sharedViewModel)
    BottomMenuBar(navController)
}

@Composable
fun DisplayMoviesColumn(sharedViewModel: SharedViewModel) {
    LazyColumn {
        val watchedMovieList = sharedViewModel.watchedlist.value
        for (i in 0..watchedMovieList.size / 3) {
            val watchedMovieSublist =
                watchedMovieList.subList(i * 3, min(i * 3 + 3, watchedMovieList.size))

            item {
                DisplayMoviesRow(watchedMovieSublist, sharedViewModel)
            }
        }
    }
}

@Composable
fun DisplayMoviesRow(movieIDList: List<Int>, sharedViewModel: SharedViewModel) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (movieIDList.size < 3) Arrangement.Start else Arrangement.SpaceEvenly
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
    Column (
        modifier = Modifier
            .width(135.dp)
            .padding(8.dp)
            .clickable {
                sharedViewModel.selectedMovie = movie
                sharedViewModel.navController?.navigate("movie_page")
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .width(125.dp)
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
            modifier = Modifier.padding(top = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}