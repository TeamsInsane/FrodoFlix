package com.frodo.frodoflix.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.frodo.frodoflix.R
import com.frodo.frodoflix.api.TMDB
import com.frodo.frodoflix.data.Movie
import com.frodo.frodoflix.viewmodels.SharedViewModel
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityPage(sharedViewModel: SharedViewModel) {
    val ratings by sharedViewModel.activityFeed.collectAsState()
    val isLoading by sharedViewModel.isLoading.collectAsState()

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = { sharedViewModel.fetchActivityFeed() }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text(
                    text = "Recent Activities",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (ratings.isEmpty()) {
                item {
                    Text(
                        text = "No activity yet.",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            items(ratings) { rating ->
                var movie by remember { mutableStateOf<Movie?>(null) }

                LaunchedEffect(rating.movieId) {
                    val movieDetails = TMDB.getDataFromTMDB("https://api.themoviedb.org/3/movie/${rating.movieId}?language=en-US", "") as? JSONObject
                    if (movieDetails != null) {
                        val id = movieDetails.getInt("id")
                        val title = movieDetails.getString("title")
                        val overview = movieDetails.getString("overview")
                        val posterPath = movieDetails.getString("poster_path")
                        val releaseDate = movieDetails.getString("release_date")
                        movie = Movie(id, title, overview, posterPath, releaseDate)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.frodo),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${rating.username}",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            val date = Date(rating.timestamp)
                            val format = SimpleDateFormat("yyyy-MM-dd, hh:mm a", java.util.Locale.getDefault())

                            Text(
                                text = format.format(date),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }


                        if (movie != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = movie!!.title,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.5f)
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

                            Box(
                                modifier = Modifier
                                    .width(140.dp)
                                    .height(200.dp)
                                    .clickable {
                                        sharedViewModel.selectedMovie = movie
                                        sharedViewModel.navController?.navigate("movie_page")
                                    }
                            ) {
                                SubcomposeAsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data("https://image.tmdb.org/t/p/w500/${movie!!.posterUrl}")
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "${movie!!.title} poster",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                ) {
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
                        }


                        Spacer(modifier = Modifier.height(8.dp))

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
}