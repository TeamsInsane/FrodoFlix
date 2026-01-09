package com.frodo.frodoflix.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.frodo.frodoflix.api.TMDB
import com.frodo.frodoflix.data.Movie
import com.frodo.frodoflix.data.UserCard
import com.frodo.frodoflix.viewmodels.SharedViewModel
import com.google.firebase.logger.Logger
import org.json.JSONArray

enum class SearchMode {
    MOVIES,
    USERS
}

@Composable
fun SearchToggle(
    selected: SearchMode,
    onSelected: (SearchMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // Movies half
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { onSelected(SearchMode.MOVIES) }
                .background(
                    if (selected == SearchMode.MOVIES)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Movies",
                fontWeight = SemiBold,
                color = if (selected == SearchMode.MOVIES)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }

        // Users half
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { onSelected(SearchMode.USERS) }
                .background(
                    if (selected == SearchMode.USERS)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Users",
                fontWeight = SemiBold,
                color = if (selected == SearchMode.USERS)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@Composable
private fun ToggleItem(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 12.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            color = if (selected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun SearchPage(sharedViewModel: SharedViewModel) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {

            SearchToggle(
                selected = sharedViewModel.searchMode,
                onSelected = { sharedViewModel.searchMode = it }
            )

            when (sharedViewModel.searchMode) {
                SearchMode.MOVIES -> MovieSearchContent(sharedViewModel)
                SearchMode.USERS -> UserSearchContent(sharedViewModel)
            }
        }
    }
}

@Composable
fun MovieSearchContent(sharedViewModel: SharedViewModel) {
    Text(
        text = "Search for movies",
        fontSize = 24.sp,
        modifier = Modifier.padding(16.dp)
    )

    DisplayMoviesSearch(
        sharedViewModel,
        sharedViewModel.movieSearchPrompt
    )
}



@Composable
fun UserSearchContent(sharedViewModel: SharedViewModel) {
    var userName by remember { mutableStateOf(sharedViewModel.userSearchPrompt) }
    var searchedUsers by remember { mutableStateOf<List<UserCard>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    Text(
        text = "Search for users",
        fontSize = 24.sp,
        modifier = Modifier.padding(16.dp)
    )

    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = userName,
            onValueChange = {
                userName = it
                sharedViewModel.userSearchPrompt = it
            },
            label = { Text("Name of user...") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
        )
    }

    // Launch search when username changes
    LaunchedEffect(userName) {
        if (userName.isNotBlank()) {
            isLoading = true

            sharedViewModel.searchUsers(userName, limit = 20) { users ->
                searchedUsers = users
                isLoading = false
            }
        } else {
            searchedUsers = emptyList()
        }
    }

    if (isLoading) {
        Text(
            "Loading...",
            modifier = Modifier.padding(16.dp)
        )
    }

    LazyColumn {
        items(searchedUsers) { user ->
            UserRowItem(user) {
                sharedViewModel.selectedUser = user
                sharedViewModel.navController?.navigate("user_page")
            }
        }
    }
}

@Composable
fun DisplayMoviesSearch(sharedViewModel: SharedViewModel, savedMovieName: String) {
    var movieName by remember { mutableStateOf(savedMovieName) }
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    LaunchedEffect(movieName) {
        if (movieName.isNotEmpty()) {
            kotlinx.coroutines.delay(300)
            movies = TMDB.getDataFromTMDB(
                "https://api.themoviedb.org/3/search/movie?query=$movieName&include_adult=false&language=en-US&page=1",
                "results"
            ) as JSONArray?
        } else {
            movies = null
        }
    }

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = movieName,
            onValueChange = {
                movieName = it
                sharedViewModel.movieSearchPrompt = movieName
            },
            label = { Text("Name of the movie ...") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
        )
    }

    if (movies != null) {
        val nonNullMovies = movies as JSONArray
        SplitSearchedMovies(nonNullMovies, sharedViewModel)
    }
}

@Composable
fun SplitSearchedMovies(movies: JSONArray, sharedViewModel: SharedViewModel) {
    val movieList = (0 until movies.length()).map { index ->
        val item = movies.getJSONObject(index)

        val id = item.getString("id").toInt()
        val overview = item.getString("overview")
        val title = item.getString("title")
        val imageUrl = item.getString("poster_path")
        val popularity = item.getDouble("popularity")
        val releaseDate = item.getString("release_date")

        Movie(id, title, overview, imageUrl, releaseDate, popularity)
    }

    val sortedMovies = movieList.sortedByDescending { it.popularity }

    val groupedMovies = sortedMovies.chunked(3)

    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        items(groupedMovies) { rowMovies ->
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ){
                for (movie in rowMovies) {
                    DisplaySearchedMovie(movie, sharedViewModel)
                }
            }
        }
    }

}

@Composable
fun DisplaySearchedMovie(movie: Movie, sharedViewModel: SharedViewModel) {
    Column (
        modifier = Modifier
            .width((LocalConfiguration.current.screenWidthDp / 3 - 8).dp)
            .padding(8.dp)
            .padding(bottom = 0.dp)
            .clickable {
                sharedViewModel.selectedMovie = movie
                sharedViewModel.navController?.navigate("movie_page")
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .width((LocalConfiguration.current.screenWidthDp / 3 - 2 * 8).dp)
                .height(180.dp)
        ) {
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
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun UserRowItem(
    user: UserCard,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal =  18.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp),
            verticalAlignment = CenterVertically
        ) {

            AsyncImage(
                model = user.profileImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = user.username,
                    fontWeight = SemiBold,
                    fontSize = 18.sp
                )

                Text(
                    text = user.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1
                )
            }
        }
    }
}