package com.frodo.frodoflix.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.frodo.frodoflix.api.TMDB
import com.frodo.frodoflix.data.Movie
import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.viewmodels.SharedViewModel
import org.json.JSONArray
import java.time.LocalDate

@Composable
fun HomePage(sharedViewModel: SharedViewModel) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        //Trending
        item {
            Text(
                text = "Trending Movies",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            TrendingMovies(sharedViewModel)
        }

        item {
            HorizontalDivider(thickness = 2.dp)
        }

        //Upcoming
        item {
            Text(
                text = "Upcoming Movies",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            UpcomingMovies(sharedViewModel)
        }

        item {
            HorizontalDivider(thickness = 2.dp)
        }

        //For You
        item {
            Text(
                text = "For You",
                color = MaterialTheme.colorScheme.onSurface,

                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            ForYouMovies(sharedViewModel)
        }

        item {
            HorizontalDivider(thickness = 2.dp)
        }

        //Family
        item {
            Text(
                text = "Family",
                color = MaterialTheme.colorScheme.onSurface,

                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            FamilyMovies(sharedViewModel)
        }

        item {
            HorizontalDivider(thickness = 2.dp)
        }

        //Horror
        item {
            Text(
                text = "Horror",
                color = MaterialTheme.colorScheme.onSurface,

                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            HorrorMovies(sharedViewModel)
        }

        item {
            HorizontalDivider(thickness = 2.dp)
        }

        //Top rated
        item {
            Text(
                text = "Top Rated",
                color = MaterialTheme.colorScheme.onSurface,

                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            TopRatedMovies(sharedViewModel)
        }

        item {
            HorizontalDivider(thickness = 15.dp, color = MaterialTheme.colorScheme.background)
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

//Trending Movies
@Composable
fun TrendingMovies(sharedViewModel: SharedViewModel) {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    LaunchedEffect(true) {
        movies = TMDB.getDataFromTMDB("https://api.themoviedb.org/3/movie/popular?language=en-US&page=1", "results") as JSONArray?
    }

    DisplayMoviesRow(movies, sharedViewModel)
}

//Upcoming Movies
@Composable
fun UpcomingMovies(sharedViewModel: SharedViewModel) {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    val minDate = LocalDate.now()
    val maxDate = minDate.plusMonths(6)

    LaunchedEffect(true) {
        movies = TMDB.getDataFromTMDB("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en&page=1&primary_release_date.gte=$minDate&primary_release_date.lte=$maxDate&sort_by=popularity.desc", "results") as JSONArray?
    }

    DisplayMoviesRow(movies, sharedViewModel)
}

//For You Movies
@Composable
fun ForYouMovies(sharedViewModel: SharedViewModel) {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    val genres = sharedViewModel.genresViewModel.getFavouriteGenreIds().joinToString("|")
    val startDate = LocalDate.now().minusYears(3)
    val url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=vote_average.desc&without_genres=99,10755&vote_count.gte=200" +
            "&with_genres=$genres" +
            "&primary_release_date.gte=$startDate"

    LaunchedEffect(true) {
        movies = TMDB.getDataFromTMDB(url, "results") as JSONArray?
    }

    DisplayMoviesRow(movies, sharedViewModel)
}

//Family Movies - 10751
@Composable
fun FamilyMovies(sharedViewModel: SharedViewModel) {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    val startDate = LocalDate.now().minusYears(2)
    val url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=vote_average.desc&without_genres=99,10755&vote_count.gte=200" +
            "&with_genres=10751" +
            "&primary_release_date.gte=$startDate"

    LaunchedEffect(true) {
        movies = TMDB.getDataFromTMDB(url, "results") as JSONArray?
    }

    DisplayMoviesRow(movies, sharedViewModel)
}

//Horror Movies - 27
@Composable
fun HorrorMovies(sharedViewModel: SharedViewModel) {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    val startDate = LocalDate.now().minusYears(2)
    val url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=vote_average.desc&without_genres=99,10755&vote_count.gte=200" +
            "&with_genres=27" +
            "&primary_release_date.gte=$startDate"

    LaunchedEffect(true) {
        movies = TMDB.getDataFromTMDB(url, "results") as JSONArray?
    }

    DisplayMoviesRow(movies, sharedViewModel)
}

//Top Rated Movies
@Composable
fun TopRatedMovies(sharedViewModel: SharedViewModel) {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    LaunchedEffect(true) {
        movies = TMDB.getDataFromTMDB("https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=1", "results") as JSONArray?
    }

    DisplayMoviesRow(movies, sharedViewModel)
}

//Display the entire row of movies
@Composable
fun DisplayMoviesRow(movies : JSONArray?, sharedViewModel: SharedViewModel) {
    if (movies != null) {
        LazyRow(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Log.d("Movies", "Size: ${movies.length()}")
            items(movies.length()) { index ->
                val item = movies.getJSONObject(index)
                Log.d("movieinfo", item.toString())
                val id = item.getString("id").toInt()
                val overview = item.getString("overview")
                val title = item.getString("title")
                val imageUrl = item.getString("poster_path")
                val releaseDate = item.getString("release_date")

                val movie = Movie(id, title, overview, imageUrl, releaseDate)

                DisplayMovie(movie, sharedViewModel)
            }
        }
    } else {
        //TODO:Let the user know
        Log.e("Movies", "Movies is null!")
    }
}

//Display each movie poster and title
@Composable
fun DisplayMovie(movie: Movie, sharedViewModel: SharedViewModel) {
    Column (
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp)
            .clickable {
                sharedViewModel.selectedMovie = movie
                sharedViewModel.navController?.navigate("movie_page")
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
