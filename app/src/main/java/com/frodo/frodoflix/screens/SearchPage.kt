package com.frodo.frodoflix.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.frodo.frodoflix.R
import com.frodo.frodoflix.api.TMDB
import com.frodo.frodoflix.data.Movie

import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.viewmodels.SharedViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray


@Composable
fun SearchPage(sharedViewModel: SharedViewModel){
    val navController = sharedViewModel.navController ?: return

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp),
        ) {
            DisplaySearch(sharedViewModel)
        }
        BottomMenuBar(navController)
    }
}

@Composable
fun DisplaySearch(sharedViewModel: SharedViewModel) {
    var movieName by remember { mutableStateOf("") }
    var showSearchedMovies by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = movieName,
            onValueChange = { movieName = it },
            label = { Text("Name of the movie ...") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        Icon(
            painter = painterResource(id = R.drawable.search),
            contentDescription = "Search",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    sharedViewModel.viewModelScope.launch {
                        if (movieName.isNotEmpty()) {
                            showSearchedMovies = true;
                        } else {
                            showSearchedMovies = false;
                        }
                    }
                }
        )
    }

    if (showSearchedMovies) {
        SearchMovies(movieName, sharedViewModel)
    }
}

@Composable
fun SearchMovies(movieName: String, sharedViewModel: SharedViewModel) {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    LaunchedEffect(true) {
        movies = TMDB.getDataFromTMDB("https://api.themoviedb.org/3/search/movie?query=$movieName", "results") as JSONArray?
    }

    DisplaySearchedMovies(movies, sharedViewModel)
}

@Composable
fun DisplaySearchedMovies(movies: JSONArray?, sharedViewModel: SharedViewModel) {
    var movieList = mutableListOf<Movie>()

    if (movies != null) {
        LazyColumn {
            items(movies.length()) { index ->
                val item = movies.getJSONObject(index)

                val id = item.getString("id").toInt()
                val overview = item.getString("overview")
                val title = item.getString("title")
                val imageUrl = item.getString("poster_path")

                val movie = Movie(id, title, overview, imageUrl)

                movieList.add(movie)

                if (movieList.size == 3) {
                    DisplaySearchedMoviesRow(movieList, sharedViewModel)
                    movieList.clear()
                }

            }
        }

    }
}

@Composable
fun DisplaySearchedMoviesRow(movieList: MutableList<Movie>, sharedViewModel: SharedViewModel) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (movieList.size < 3) Arrangement.Start else Arrangement.SpaceEvenly
    ){
        for (movie in movieList) {
            DisplaySearchedMovie(movie, sharedViewModel)
        }
    }
}

@Composable
fun DisplaySearchedMovie(movie: Movie, sharedViewModel: SharedViewModel) {
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
                .width(120.dp)
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