package com.frodo.frodoflix.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.frodo.frodoflix.api.TMDB
import org.json.JSONArray
import java.time.LocalDate

@Composable
fun DrawMainPage(navController: NavController) {

    Scaffold {innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(
                text = "Trending Movies",
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )

            TrendingMovies()

            HorizontalDivider(thickness = 2.dp)

            Text(
                text = "Upcoming Movies",
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )

            UpcomingMovies()

            HorizontalDivider(thickness = 2.dp)

            Text(
                text = "For You ",
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )

        }

        BottomMenuBar(innerPadding, navController)
    }
}

//Trending Movies
@Composable
fun TrendingMovies() {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    LaunchedEffect(true) {
        movies = TMDB.getDataFromTMDB("https://api.themoviedb.org/3/movie/popular?language=en-US&page=1", "results")
    }

    DisplayMoviesRow(movies)
}

//Upcoming Movies
@Composable
fun UpcomingMovies() {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    val minDate = LocalDate.now()
    val maxDate = minDate.plusMonths(6)

    //Fetch movie data from the TMDB API
    LaunchedEffect(true) {
        movies = TMDB.getDataFromTMDB("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en&page=1&primary_release_date.gte=$minDate&primary_release_date.lte=$maxDate&sort_by=popularity.desc", "results")
    }

    DisplayMoviesRow(movies)
}

//Display the entire row of movies
@Composable
fun DisplayMoviesRow(movies : JSONArray?) {
    if (movies != null) {
        LazyRow(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Log.d("Movies", "Size: ${movies.length()}")
            items(movies.length()) { index ->
                val item = movies.getJSONObject(index)
                val title = item.getString("original_title")
                val imageUrl = item.getString("poster_path")

                DisplayMovie(title = title, imageUrl = imageUrl)
            }
        }
    } else {
        //TODO:Let the user know
        Log.e("Movies", "Movies is null!")
    }
}

//Display each movie poster and title
@Composable
fun DisplayMovie(title: String, imageUrl: String) {
    Log.d("Movies", "$title $imageUrl")
    Column (
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp)
            .clickable {
                Log.d("movieclick", "Clicked on movie $title $imageUrl")
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .width(140.dp)
                .height(200.dp)
        ) {
            // Loading states for images (loading image before the image is loaded...)
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500/$imageUrl")
                    .crossfade(true)
                    .build(),
                contentDescription = "$title poster",
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
            text = title,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

}

@Composable
fun BottomMenuBar(innerPadding : PaddingValues, navController : NavController) {
    //TODO: Bottom Menu Bar
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {

        // Row to place buttons horizontally next to each other
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { }
            ) {
                Text(text = "Nedela")
            }
            Button(
                onClick = { navController.navigate("profile") }
            ) {
                Text(text = "Profile")
            }
        }
    }
}