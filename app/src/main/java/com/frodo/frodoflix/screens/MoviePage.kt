package com.frodo.frodoflix.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.frodo.frodoflix.R
import com.frodo.frodoflix.api.TMDB
import com.frodo.frodoflix.data.Actor
import com.frodo.frodoflix.data.Movie
import com.frodo.frodoflix.data.Rating
import com.frodo.frodoflix.viewmodels.SharedViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DisplayMoviePage(sharedViewModel: SharedViewModel) {
    var data by remember { mutableStateOf<JSONObject?>(null) }
    val movie = sharedViewModel.selectedMovie
    val navController = sharedViewModel.navController ?: return

    if (movie == null) {
        Log.d("movie", "Movie is null")
        return
    }

    LaunchedEffect(true) {
        data = TMDB.getDataFromTMDB(
            "https://api.themoviedb.org/3/movie/${movie.id}?language=en-US",
            ""
        ) as? JSONObject
    }

    if (data == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val nonNullData = data as JSONObject

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        item {
            // Hero Section with Backdrop
            ModernMovieHero(
                backdropPath = nonNullData.getString("backdrop_path"),
                posterPath = movie.posterUrl,
                title = movie.title,
                rating = nonNullData.getString("vote_average").toDouble(),
                navController = navController,
                sharedViewModel = sharedViewModel,
                movie = movie
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Movie Info Section
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // Title
                Text(
                    text = movie.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Meta Info Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rating Badge
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = BigDecimal(nonNullData.getString("vote_average").toDouble())
                                    .setScale(1, RoundingMode.HALF_EVEN).toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Runtime
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.timelapse),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${nonNullData.getString("runtime")} min",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Release Date
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.calendar_month),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = movie.releaseDate.take(4),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Overview
                Text(
                    text = nonNullData.getString("overview"),
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                ModernActionButtons(sharedViewModel, navController, movie)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Cast Section
            Column {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎭",
                        fontSize = 22.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Cast",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                CastSection(movie.id)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ratings Section
            Column {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⭐",
                        fontSize = 22.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Reviews",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                ModernRatingsSection(sharedViewModel)
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun ModernMovieHero(
    backdropPath: String,
    posterPath: String,
    title: String,
    rating: Double,
    navController: androidx.navigation.NavController,
    sharedViewModel: SharedViewModel,
    movie: Movie
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    ) {
        // Backdrop Image
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://image.tmdb.org/t/p/original/$backdropPath")
                .crossfade(true)
                .build(),
            contentDescription = title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
                is AsyncImagePainter.State.Success -> {
                    SubcomposeAsyncImageContent()
                }
                else -> {}
            }
        }

        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.95f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )

        // Back Button
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        // Poster and Quick Actions at Bottom
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Poster
            Card(
                modifier = Modifier
                    .width(120.dp)
                    .height(180.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://image.tmdb.org/t/p/w500/$posterPath")
                        .crossfade(true)
                        .build(),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                ) {
                    when (painter.state) {
                        is AsyncImagePainter.State.Success -> {
                            SubcomposeAsyncImageContent()
                        }
                        else -> {}
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Quick Actions
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val favList by sharedViewModel.favList.collectAsState()
                val watchedList by sharedViewModel.watchedList.collectAsState()

                // Favorite Button
                FloatingActionButton(
                    onClick = { sharedViewModel.updateFavList(movie.id) },
                    containerColor = if (favList.contains(movie.id))
                        Color.Red.copy(alpha = 0.9f)
                    else
                        Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = if (favList.contains(movie.id))
                            Icons.Filled.Favorite
                        else
                            Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (favList.contains(movie.id))
                            Color.White
                        else
                            Color.Red
                    )
                }

                // Watched Button
                FloatingActionButton(
                    onClick = { sharedViewModel.updateWatchedlist(movie.id) },
                    containerColor = if (watchedList.contains(movie.id))
                        Color(0xFF4CAF50).copy(alpha = 0.9f)
                    else
                        Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.circle_gray),
                        contentDescription = "Watched",
                        tint = if (watchedList.contains(movie.id))
                            Color.White
                        else
                            Color(0xFF4CAF50)
                    )
                }
            }
        }
    }
}

@Composable
fun ModernActionButtons(
    sharedViewModel: SharedViewModel,
    navController: androidx.navigation.NavController,
    movie: Movie
) {
    val starsRated = remember { mutableIntStateOf(-1) }
    val watchlist by sharedViewModel.watchlist.collectAsState()
    var showShareDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        starsRated.intValue = sharedViewModel.getStarsRated()
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Rate Button
        Button(
            onClick = { navController.navigate("rate_movie") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (starsRated.intValue != -1)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (starsRated.intValue != -1) "Rated: ${starsRated.intValue}" else "Rate",
                fontWeight = FontWeight.SemiBold
            )
        }

        // Watchlist Button
        OutlinedButton(
            onClick = { sharedViewModel.updateWatchlist(movie.id) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (watchlist.contains(movie.id))
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                else
                    Color.Transparent
            )
        ) {
            Icon(
                painter = painterResource(
                    id = if (watchlist.contains(movie.id))
                        R.drawable.bookmark_filled
                    else
                        R.drawable.bookmark_outline
                ),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }

        // Share Button
        OutlinedButton(
            onClick = { showShareDialog = true },
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "Share",
                modifier = Modifier.size(20.dp)
            )
        }
    }

    if (showShareDialog) {
        ShareMovieDialog(
            sharedViewModel = sharedViewModel,
            onDismiss = { showShareDialog = false }
        )
    }
}

@Composable
fun CastSection(movieID: Int) {
    var castData by remember { mutableStateOf<JSONArray?>(null) }

    LaunchedEffect(true) {
        castData = TMDB.getDataFromTMDB(
            "https://api.themoviedb.org/3/movie/$movieID/credits?language=en-US",
            "cast"
        ) as JSONArray?
    }

    if (castData != null) {
        LazyRow(
            modifier = Modifier.padding(start = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items((0 until minOf(castData!!.length(), 10)).map { index ->
                val item = castData!!.getJSONObject(index)
                Actor(
                    item.getString("name"),
                    item.getString("character"),
                    item.getString("profile_path")
                )
            }) { actor ->
                ModernActorCard(actor)
            }
            item {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun ModernActorCard(actor: Actor) {
    Column(
        modifier = Modifier.width(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .size(100.dp),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500/${actor.profilePath}")
                    .crossfade(true)
                    .build(),
                contentDescription = actor.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center).size(24.dp)
                            )
                        }
                    }
                    is AsyncImagePainter.State.Success -> {
                        SubcomposeAsyncImageContent()
                    }
                    else -> {}
                }
            }
        }

        Text(
            text = actor.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp),
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = actor.character,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun ModernRatingsSection(sharedViewModel: SharedViewModel) {
    val ratingList = remember { mutableStateListOf<Rating>() }

    LaunchedEffect(true) {
        val fetchedRatingList = sharedViewModel.getRatingList()
        ratingList.clear()
        ratingList.addAll(fetchedRatingList)
    }

    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ratingList.take(5).forEach { rating ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.tertiary
                                        )
                                    ),
                                    shape = CircleShape
                                )
                                .padding(2.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = rating.username,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            val date = Date(rating.timestamp)
                            val format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                            Text(
                                text = format.format(date),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Rating
                        Row {
                            repeat(rating.rating) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    if (rating.comment.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = rating.comment,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShareMovieDialog(sharedViewModel: SharedViewModel, onDismiss: () -> Unit) {
    val groups by sharedViewModel.groups.collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }

    val filteredGroups = if (searchQuery.isEmpty()) {
        groups
    } else {
        groups.filter { it.groupName.contains(searchQuery, ignoreCase = true) }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Share Movie",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Groups") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    items(filteredGroups) { group ->
                        Surface(
                            onClick = {
                                sharedViewModel.selectedMovie?.let { movie ->
                                    sharedViewModel.sendMessage(group.groupId, "$${movie.id}$")
                                }
                                sharedViewModel.navController?.navigate("chat_page/${group.groupId}")
                                onDismiss()
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = group.groupName,
                                    modifier = Modifier.weight(1f),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send"
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}