package com.frodo.frodoflix.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.frodo.frodoflix.data.Rating
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.viewmodels.SharedViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode

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

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                BackToPreviousScreen(navController)
                DisplayMovieBanner(nonNullData.getString("backdrop_path"), sharedViewModel, movie)

                // Title Text
                Text(
                    text = movie.title,
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Rating, Duration, Release Date
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = BigDecimal(nonNullData.getString("vote_average").toDouble()).setScale(1, RoundingMode.HALF_EVEN).toString() + "/10",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Duration
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.timelapse),
                            contentDescription = "Duration Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${nonNullData.getString("runtime")} min",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Release Date
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.calendar_month),
                            contentDescription = "Release Date Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = movie.releaseDate,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Description Text
                Text(
                    text = nonNullData.getString("overview"),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                )

                DisplayRateMovie(sharedViewModel, navController, movie)

                HorizontalDivider(thickness = 2.dp)

                // Cast Section Title
                Text(
                    text = "Cast",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    fontWeight = FontWeight.Bold
                )

                CastData(movie.id)

                HorizontalDivider(thickness = 2.dp)


                // Rating Section Title
                Text(
                    text = "Ratings",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    fontWeight = FontWeight.Bold
                )

                HorizontalDivider(thickness = 1.dp)

                DisplayRatings(sharedViewModel)

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }

    BottomMenuBar(navController)
}


@Composable
fun DisplayMovieBanner(bannerPath: String, sharedViewModel: SharedViewModel, movie: Movie) {
    Box(modifier = Modifier.fillMaxWidth()) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://image.tmdb.org/t/p/original/$bannerPath")
                .crossfade(true)
                .build(),
            contentDescription = "Movie banner",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            contentScale = ContentScale.Crop
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

        val favList by sharedViewModel.favList.collectAsState()
        val isInFavList = favList.contains(movie.id)

        val watchedList by sharedViewModel.watchedList.collectAsState()
        val isInWatchedList = watchedList.contains(movie.id)

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.circle_gray),
                contentDescription = "Watched Icon",
                tint = if (isInWatchedList) Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable {
                        sharedViewModel.updateWatchedlist(movieID = movie.id)
                    }
            )

            Icon(
                imageVector = if (isInFavList) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite Icon",
                tint = Color.Red,
                modifier = Modifier
                    .padding(16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable {
                        sharedViewModel.updateFavList(movieID = movie.id)
                    }
            )
        }
    }
}



@Composable
fun DisplayRateMovie(sharedViewModel: SharedViewModel, navController: NavController, movie: Movie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val starsRated = remember { mutableIntStateOf(-1) }

        LaunchedEffect(true) {
            starsRated.intValue = sharedViewModel.getStarsRated()
            starsRated.intValue = sharedViewModel.getStarsRated()
        }

        Button(
            onClick = {
                navController.navigate("rate_movie")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (starsRated.intValue != -1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "star icon",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (starsRated.intValue != -1) "Rated: ${starsRated.intValue}" else "Rate Movie",
                fontWeight = FontWeight.SemiBold
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
                containerColor = if (isInWatchlist) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = if (isInWatchlist) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "watchlist icon",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isInWatchlist) "In Watchlist" else "Add to Watchlist",
                fontWeight = FontWeight.SemiBold
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

    ReadCastData(castData)
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

@Composable
fun DisplayRatings(sharedViewModel: SharedViewModel) {
    val ratingList = remember { mutableStateListOf<Rating>() }

    LaunchedEffect(true) {
        val fetchedRatingList = sharedViewModel.getRatingList()
        ratingList.clear()
        for (rating in fetchedRatingList) {
            ratingList.add(rating)
        }
    }

    Column (
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        for (rating in ratingList) {
            Log.d("rating", "Smo notr")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Profile Image
                Image(
                    painter = painterResource(id = R.drawable.frodo),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Username and Rating
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Review by ${rating.username}",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        repeat(rating.rating) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Comment
                    Text(
                        text = rating.comment,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            HorizontalDivider(thickness = 1.dp)
        }

    }
}