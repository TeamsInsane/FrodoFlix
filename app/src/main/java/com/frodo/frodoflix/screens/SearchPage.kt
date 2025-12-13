package com.frodo.frodoflix.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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

import org.json.JSONArray

@Composable
fun SearchPage(sharedViewModel: SharedViewModel){
    val navController = sharedViewModel.navController ?: return
    val savedMovieName = sharedViewModel.searchPrompt

     LazyColumn {
        item {
            Text(
                text = "Search for movies",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            DisplaySearch(sharedViewModel, savedMovieName)
        }
    }
}

@Composable
fun DisplaySearch(sharedViewModel: SharedViewModel, savedMovieName: String) {
    var movieName by remember { mutableStateOf(savedMovieName) }
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    LaunchedEffect(movieName) {
        if (movieName.isNotEmpty()) {
            kotlinx.coroutines.delay(300)
            movies = TMDB.getDataFromTMDB("https://api.themoviedb.org/3/search/movie?query=$movieName&include_adult=false&language=en-US&page=1", "results") as JSONArray?
        } else {
            movies = null
        }
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = movieName,
                onValueChange = { movieName = it; sharedViewModel.searchPrompt = movieName },
                label = { Text("Name of the movie ...") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
        }

        if (movies != null) {
            val nonNullMovies = movies as JSONArray
            SplitSearchedMovies(nonNullMovies, sharedViewModel)
        }
    }
}

@Composable
fun SplitSearchedMovies(movies: JSONArray, sharedViewModel: SharedViewModel) {
    val movieList = (0 until movies.length()).map { index ->
        val item = movies.getJSONObject(index)

        val id = item.getString("id").toInt()
        val overview = item.getString("overview")
        val title = item.getString("title")
        val imageUrl = item.getString("poster_path")
        val popularity = item.getDouble("popularity")
        val releaseDate = item.getString("release_date")

        Movie(id, title, overview, imageUrl, releaseDate, popularity)
    }

    val sortedMovies = movieList.sortedByDescending { it.popularity }

    val groupedMovies = sortedMovies.chunked(3)

    LazyColumn(
        modifier = Modifier.padding(8.dp).height(500.dp) // TODO: Fix this, not ideal
    ) {
        items(groupedMovies) { rowMovies ->
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ){
                for (movie in rowMovies) {
                    DisplaySearchedMovie(movie, sharedViewModel)
                }
            }
        }
    }

}

@Composable
fun DisplaySearchedMovie(movie: Movie, sharedViewModel: SharedViewModel) {
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