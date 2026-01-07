package com.frodo.frodoflix.screens.user

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.frodo.frodoflix.api.TMDB
import com.frodo.frodoflix.data.Movie
import com.frodo.frodoflix.screens.profile.DisplayMovie
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.viewmodels.SharedViewModel
import org.json.JSONObject

@Composable
fun DisplayUserPage(sharedViewModel: SharedViewModel) {
    val user = sharedViewModel.selectedUser
    val navController = sharedViewModel.navController ?: return

    if (user == null) return


    LazyColumn {
        item {
            BackToPreviousScreen(navController)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                // Avatar
                Image(
                    painter = rememberAsyncImagePainter(user.profileImageUrl),
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Name
                Text(
                    text = user.username,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                var isFollowed by remember { mutableStateOf(false) }
                var followersCount by remember { mutableIntStateOf(user.followersCount) }

                LaunchedEffect(user.username) {
                    isFollowed = sharedViewModel.doesUserFollow(user)
                    followersCount = user.followersCount
                }

                // Followers / Following
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$followersCount", fontWeight = FontWeight.Bold)
                        Text(text = "Followers", fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.width(32.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${user.followingCount}", fontWeight = FontWeight.Bold)
                        Text(text = "Following", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Follow / Unfollow Button
                Button(
                    onClick = {
                        if (isFollowed) {
                            followersCount -= 1
                        } else {
                            followersCount += 1
                        }
                        isFollowed = !isFollowed

                        sharedViewModel.toggleFollow(user)
                    },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(if (isFollowed) "Unfollow" else "Follow")
                }
            }
        }

        // WATCHLIST
        item {
            Text(
                text = "Watchlist",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        items(user.watchlist) { movie ->
            MovieRowItem(movie)
        }

        // Fav LIST
        item {
            Text(
                text = "Watched",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        items(user.favlist) { movie ->
            MovieRowItem(movie)
        }
    }
}

suspend fun getMovieFromTMDB(movieId: Int): JSONObject? {
    return try {
        val url = "https://api.themoviedb.org/3/movie/$movieId?language=en-US"
        val data = TMDB.getDataFromTMDB(url, "")
        data as? JSONObject
    } catch (e: Exception) {
        null
    }
}

@Composable
fun MovieRowItem(movieId: Int) {
    var movieJson by remember { mutableStateOf<JSONObject?>(null) }

    LaunchedEffect(movieId) {
        movieJson = getMovieFromTMDB(movieId)
    }

    if (movieJson == null) {
        Text("Loading...")
        return
    }

    val title = movieJson!!.getString("title")
    val posterUrl = "https://image.tmdb.org/t/p/w500" + movieJson!!.getString("poster_path")
    val releaseDate = movieJson!!.getString("release_date")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(posterUrl),
            contentDescription = "Movie Poster",
            modifier = Modifier.size(60.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(releaseDate, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        IconButton(onClick = { /* TODO: mark as watched */ }) {
            Icon(Icons.Default.Check, contentDescription = "Mark as watched")
        }
    }
}