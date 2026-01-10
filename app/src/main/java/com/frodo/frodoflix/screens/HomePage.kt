package com.frodo.frodoflix.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.frodo.frodoflix.api.TMDB
import com.frodo.frodoflix.data.Movie
import com.frodo.frodoflix.viewmodels.SharedViewModel
import org.json.JSONArray
import java.time.LocalDate

@Composable
fun HomePage(sharedViewModel: SharedViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        // Featured Movie Hero Section
        item {
            FeaturedMovieSection(sharedViewModel)
        }

        // Now Showing
        item {
            SectionHeader(
                title = "Now Showing",
                icon = "🎬",
                onSeeMoreClick = { }
            )
            TrendingMovies(sharedViewModel)
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Upcoming
        item {
            SectionHeader(
                title = "Coming Soon",
                icon = "🔜",
                onSeeMoreClick = { }
            )
            UpcomingMovies(sharedViewModel)
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        // For You
        item {
            SectionHeader(
                title = "For You",
                icon = "✨",
                onSeeMoreClick = { }
            )
            ForYouMovies(sharedViewModel)
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Family
        item {
            SectionHeader(
                title = "Family",
                icon = "👨‍👩‍👧‍👦",
                onSeeMoreClick = { }
            )
            FamilyMovies(sharedViewModel)
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Horror
        item {
            SectionHeader(
                title = "Horror",
                icon = "😱",
                onSeeMoreClick = { }
            )
            HorrorMovies(sharedViewModel)
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Top Rated
        item {
            SectionHeader(
                title = "Top Rated",
                icon = "⭐",
                onSeeMoreClick = { }
            )
            TopRatedMovies(sharedViewModel)
        }

        item { Spacer(modifier = Modifier.height(100.dp)) }
    }
}

@Composable
fun FeaturedMovieSection(sharedViewModel: SharedViewModel) {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    LaunchedEffect(true) {
        movies = TMDB.getDataFromTMDB(
            "https://api.themoviedb.org/3/movie/popular?language=en-US&page=1",
            "results"
        ) as JSONArray?
    }

    if (movies != null && movies!!.length() > 0) {
        val item = movies!!.getJSONObject(0)
        val id = item.getString("id").toInt()
        val overview = item.getString("overview")
        val title = item.getString("title")
        val backdropPath = item.getString("backdrop_path")
        val releaseDate = item.getString("release_date")
        val voteAverage = item.getDouble("vote_average")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(bottom = 16.dp)
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
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
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
                                Color.Black.copy(alpha = 0.9f)
                            ),
                            startY = 0f,
                            endY = 1000f
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                // NOW SHOWING Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Surface(
                        color = Color.Red,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "NOW SHOWING",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    // Rating Badge
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = String.format("%.1f", voteAverage),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = releaseDate.take(4),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            sharedViewModel.selectedMovie = Movie(
                                id, title, overview,
                                item.getString("poster_path"), releaseDate
                            )
                            sharedViewModel.navController?.navigate("movie_page")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(25.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "View Details",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    icon: String,
    onSeeMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = icon,
                fontSize = 24.sp
            )
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(onClick = onSeeMoreClick) {
            Text(
                text = "See more →",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun TrendingMovies(sharedViewModel: SharedViewModel) {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    LaunchedEffect(true) {
        movies = TMDB.getDataFromTMDB(
            "https://api.themoviedb.org/3/movie/popular?language=en-US&page=1",
            "results"
        ) as JSONArray?
    }

    DisplayMoviesRow(movies, sharedViewModel)
}

@Composable
fun UpcomingMovies(sharedViewModel: SharedViewModel) {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    val minDate = LocalDate.now()
    val maxDate = minDate.plusMonths(6)

    LaunchedEffect(true) {
        movies = TMDB.getDataFromTMDB(
            "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en&page=1&primary_release_date.gte=$minDate&primary_release_date.lte=$maxDate&sort_by=popularity.desc",
            "results"
        ) as JSONArray?
    }

    DisplayMoviesRow(movies, sharedViewModel)
}

@Composable
fun ForYouMovies(sharedViewModel: SharedViewModel) {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    val genres = sharedViewModel.genresViewModel.getFavouriteGenreIds().joinToString("|")
    val startDate = LocalDate.now().minusYears(3)
    val url =
        "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=vote_average.desc&without_genres=99,10755&vote_count.gte=200" +
                "&with_genres=$genres" +
                "&primary_release_date.gte=$startDate"

    LaunchedEffect(true) {
        movies = TMDB.getDataFromTMDB(url, "results") as JSONArray?
    }

    DisplayMoviesRow(movies, sharedViewModel)
}

@Composable
fun FamilyMovies(sharedViewModel: SharedViewModel) {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    val startDate = LocalDate.now().minusYears(2)
    val url =
        "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=vote_average.desc&without_genres=99,10755&vote_count.gte=200" +
                "&with_genres=10751" +
                "&primary_release_date.gte=$startDate"

    LaunchedEffect(true) {
        movies = TMDB.getDataFromTMDB(url, "results") as JSONArray?
    }

    DisplayMoviesRow(movies, sharedViewModel)
}

@Composable
fun HorrorMovies(sharedViewModel: SharedViewModel) {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    val startDate = LocalDate.now().minusYears(2)
    val url =
        "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=vote_average.desc&without_genres=99,10755&vote_count.gte=200" +
                "&with_genres=27" +
                "&primary_release_date.gte=$startDate"

    LaunchedEffect(true) {
        movies = TMDB.getDataFromTMDB(url, "results") as JSONArray?
    }

    DisplayMoviesRow(movies, sharedViewModel)
}

@Composable
fun TopRatedMovies(sharedViewModel: SharedViewModel) {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    LaunchedEffect(true) {
        movies = TMDB.getDataFromTMDB(
            "https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=1",
            "results"
        ) as JSONArray?
    }

    DisplayMoviesRow(movies, sharedViewModel)
}

@Composable
fun DisplayMoviesRow(movies: JSONArray?, sharedViewModel: SharedViewModel) {
    if (movies != null) {
        LazyRow(
            modifier = Modifier.padding(start = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(movies.length()) { index ->
                val item = movies.getJSONObject(index)
                val id = item.getString("id").toInt()
                val overview = item.getString("overview")
                val title = item.getString("title")
                val imageUrl = item.getString("poster_path")
                val releaseDate = item.getString("release_date")
                val voteAverage = item.optDouble("vote_average", 0.0)

                val movie = Movie(id, title, overview, imageUrl, releaseDate)

                ModernMovieCard(movie, voteAverage, sharedViewModel)
            }
            item {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun ModernMovieCard(
    movie: Movie,
    rating: Double,
    sharedViewModel: SharedViewModel
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(240.dp)
            .clickable {
                sharedViewModel.selectedMovie = movie
                sharedViewModel.navController?.navigate("movie_page")
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500/${movie.posterUrl}")
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
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
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    is AsyncImagePainter.State.Success -> {
                        SubcomposeAsyncImageContent()
                    }
                    else -> {}
                }
            }

            // Gradient overlay at bottom
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    )
            )

            // Rating badge
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                color = Color.Black.copy(alpha = 0.7f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%.1f", rating),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Title
            Text(
                text = movie.title,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )
        }
    }
}
