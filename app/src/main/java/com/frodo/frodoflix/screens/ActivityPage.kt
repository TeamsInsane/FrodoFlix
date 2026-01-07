package com.frodo.frodoflix.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.frodo.frodoflix.R
import com.frodo.frodoflix.api.TMDB
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
            items(ratings) { it ->
                var movieTitle by remember { mutableStateOf("") }

                LaunchedEffect(it.movieId) {
                    val movieDetails = TMDB.getDataFromTMDB("https://api.themoviedb.org/3/movie/${it.movieId}", "") as? JSONObject
                    movieTitle = movieDetails?.getString("title") ?: ""
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
                                text = "Review by ${it.username}",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            repeat(it.rating) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Star",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Text(
                            text = movieTitle,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        val date = Date(it.timestamp)
                        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                        Text(
                            text = format.format(date),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )


                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = it.comment,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }

                HorizontalDivider(thickness = 1.dp)
            }
        }
    }
}