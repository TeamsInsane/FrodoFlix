package com.frodo.frodoflix.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.frodo.frodoflix.api.TMDB
import com.frodo.frodoflix.data.Message
import com.frodo.frodoflix.data.Movie
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.viewmodels.SharedViewModel
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPage(sharedViewModel: SharedViewModel, groupId: String) {
    val messages by sharedViewModel.messages.collectAsState()
    var newMessage by remember { mutableStateOf("") }
    val navController = sharedViewModel.navController ?: return
    val currentUser = sharedViewModel.getUsername()

    LaunchedEffect(groupId) {
        sharedViewModel.listenForMessages(groupId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BackToPreviousScreen(navController)

        LazyColumn(
            modifier = Modifier
            .weight(1f)
            .padding(horizontal = 8.dp),
        ) {
            items(messages) {
                message ->
                MessageBubble(message = message, isCurrentUser = message.username == currentUser, sharedViewModel = sharedViewModel)
            }
        }

        TextField(
            value = newMessage,
            onValueChange = { newMessage = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            placeholder = { Text("Send a message") },
            trailingIcon = {
                IconButton(onClick = {
                    if (newMessage.isNotBlank()) {
                        sharedViewModel.sendMessage(groupId, newMessage)
                        newMessage = ""
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send"
                    )
                }
            }
        )
    }
}

@Composable
fun MessageBubble(message: Message, isCurrentUser: Boolean, sharedViewModel: SharedViewModel) {
    val bubbleColor = if (isCurrentUser) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surfaceContainer
    val textColor = if (isCurrentUser) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
    val movieIdPattern = """\$(.*?)\$""".toRegex()
    val matchResult = movieIdPattern.find(message.content)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(bubbleColor)
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = message.username,
                    fontSize = 12.sp,
                    color = textColor
                )
                if (matchResult != null) {
                    val movieId = matchResult.destructured.component1()
                    SharedMovie(movieId = movieId, sharedViewModel = sharedViewModel)
                } else {
                    Text(
                        text = message.content,
                        color = textColor
                    )
                }
            }
        }
    }
}

@Composable
fun SharedMovie(movieId: String, sharedViewModel: SharedViewModel) {
    var movie by remember { mutableStateOf<Movie?>(null) }

    LaunchedEffect(movieId) {
        val data = TMDB.getDataFromTMDB("https://api.themoviedb.org/3/movie/$movieId?language=en-US", "") as? JSONObject
        if (data != null) {
            val id = data.getInt("id")
            val title = data.getString("title")
            val overview = data.getString("overview")
            val posterPath = data.getString("poster_path")
            val releaseDate = data.getString("release_date")
            movie = Movie(id, title, overview, posterPath, releaseDate)
        }
    }

    if (movie != null) {
        DisplayInlineMovie(movie!!, sharedViewModel)
    } else {
        CircularProgressIndicator()
    }
}

@Composable
fun DisplayInlineMovie(movie: Movie, sharedViewModel: SharedViewModel) {
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
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500/${movie.posterUrl}")
                    .crossfade(true)
                    .build(),
                contentDescription = "${movie.title} poster",
                modifier = Modifier.fillMaxWidth()
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

        Text(
            text = movie.title,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
