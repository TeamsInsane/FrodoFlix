package com.frodo.frodoflix.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.frodo.frodoflix.R
import com.frodo.frodoflix.api.TMDB
import com.frodo.frodoflix.data.Actor
import com.frodo.frodoflix.data.Movie
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.viewmodels.SharedViewModel
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun DisplayMoviePage(sharedViewModel: SharedViewModel) {
    var data by remember { mutableStateOf<JSONObject?>(null) }
    val movie = sharedViewModel.selectedMovie
    val navController = sharedViewModel.navController ?: return

    if (movie == null) {
        Log.d("movie", "NULLLLL!")
        return
    }

    LaunchedEffect(true) {
        data = TMDB.getDataFromTMDB("https://api.themoviedb.org/3/movie/${movie.id}?language=en-US", "") as? JSONObject
    }

    if (data == null) {
        Log.d("movie", "TUDDNULL")
        return
    }


    val nonNullData = data as JSONObject
    // Apply Scaffold for consistent theming and structure
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                BackToPreviousScreen(navController)
                DisplayMovieBanner(nonNullData.getString("backdrop_path"))

                // Title Text
                Text(
                    text = movie.title,
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp),
                    fontWeight = FontWeight.Bold
                )

                // Rating and Duration Text
                Text(
                    text = "Rating: ${nonNullData.getString("vote_average")}     ${nonNullData.getString("runtime")} min",
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                )

                // Description Text
                Text(
                    text = nonNullData.getString("overview"),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                )

                DisplayRateMovie(sharedViewModel, navController, movie)

                // Cast Section Title
                Text(
                    text = "Cast",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    fontWeight = FontWeight.Bold
                )

                CastData(movie.id)
            }
        }
    }

    BottomMenuBar(navController)
}


@Composable
fun DisplayMovieBanner(bannerPath: String) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://image.tmdb.org/t/p/original/$bannerPath")
            .crossfade(true)
            .build(),
        contentDescription = "Movie banner",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
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



@Composable
fun DisplayRateMovie(sharedViewModel: SharedViewModel, navController: NavController, movie: Movie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.Center, // Center horizontally
        verticalAlignment = Alignment.CenterVertically // Align vertically
    ) {
        val watchedlist by sharedViewModel.watchedlist.collectAsState()

        val isInWatchedlist = watchedlist.contains(movie.id)
        // Watched + rate movie button
        Button(
            onClick = {
                navController.navigate("rate_movie")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isInWatchedlist) Color.Green else Color.Yellow,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = if (isInWatchedlist) "already rated" else "rate"
            )
        }

        //Watchlist button
        val watchlist by sharedViewModel.watchlist.collectAsState()

        val isInWatchlist = watchlist.contains(movie.id)

        Button(
            onClick = {
                sharedViewModel.updateWatchlist(movie.id)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isInWatchlist) Color.Green else Color.Yellow,
                contentColor = Color.Black
            ),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = if (isInWatchlist) "Remove from Watchlist" else "Add to Watchlist"
            )
        }
    }
}


@Composable
fun CastData(movieID: Int) {
    var castData by remember { mutableStateOf<JSONArray?>(null) }

    //Fetch movie data from the TMDB API
    LaunchedEffect(true) {
        castData = TMDB.getDataFromTMDB("https://api.themoviedb.org/3/movie/$movieID/credits?language=en-US", "cast") as JSONArray?
    }

    ReadCastData(castData);
}

@Composable
fun ReadCastData(data: JSONArray?) {
    if (data == null) {
        Log.d("movie", "DATA ISN UL!L!L!")
        return
    }

    LazyRow(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        items(data.length()) { index ->
            val item = data.getJSONObject(index)

            val name = item.getString("name")
            val character = item.getString("character")
            val profilePath = item.getString("profile_path")

            val actor = Actor(name, character, profilePath)

            DisplayActor(actor)
        }
    }
}

@Composable
fun DisplayActor(actor: Actor) {

    Column (
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(200.dp)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500/${actor.profilePath}")
                    .crossfade(true)
                    .build(),
                contentDescription = "${actor.name} profile picture",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
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

        //Actor name
        Text(
            text = actor.name,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold
        )

        //Character name
        Text(
            text = actor.character,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}