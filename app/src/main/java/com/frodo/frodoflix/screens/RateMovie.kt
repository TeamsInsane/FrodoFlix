package com.frodo.frodoflix.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.frodo.frodoflix.data.Movie
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.viewmodels.SharedViewModel
import kotlinx.coroutines.launch

@Composable
fun RateMovie(sharedViewModel: SharedViewModel) {
    val navController = sharedViewModel.navController ?: return
    var selectedRating by remember { mutableIntStateOf(0) }
    var writtenComment by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            BackToPreviousScreen(navController)

            Spacer(modifier = Modifier.height(16.dp))

            //Rate movie text
            Text(
                text = "Rate Movie",
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Movie details and image
            sharedViewModel.selectedMovie?.let { DisplayMovieDetails(it) }

            Spacer(modifier = Modifier.height(16.dp))

            //Option to select rating
            DisplayRatingDropdown { rating ->
                if (rating != null) {
                    selectedRating = rating
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Option to write a review
            DisplayReview { comment ->
                if (comment != null) {
                    writtenComment = comment
                }
            }

            //Button to save
            DisplaySaveReview(sharedViewModel, selectedRating, writtenComment)
        }

        BottomMenuBar(navController)
    }
}

@Composable
fun DisplayMovieDetails(movie: Movie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(180.dp)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500/${movie.posterUrl}")
                    .crossfade(true)
                    .build(),
                contentDescription = "${movie.title} poster",
                modifier = Modifier.size(180.dp)
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

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2
            )
            Text(
                text = "Rate and review this movie.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DisplayReview(onResult: (String?) -> Unit) {
    var comment by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Write a Review:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextField(
            value = comment,
            onValueChange = {
                comment = it
                onResult(comment )
            },
            label = { Text("Add a review...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
        )
    }
}

@Composable
fun DisplayRatingDropdown(onResult: (Int?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedRating by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Select a Rating:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(8.dp, 0.dp, 8.dp, 8.dp)
                .height(56.dp)
        ) {
            Text(
                text = if (selectedRating != 0) "Rating: $selectedRating ⭐" else "No rating",
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                (1..5).forEach { rating ->
                    DropdownMenuItem(
                        text = { Text(text = "$rating ⭐") },
                        onClick = {
                            selectedRating = rating
                            expanded = false
                            onResult(selectedRating)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DisplaySaveReview(sharedViewModel: SharedViewModel, selectedRating: Int, comment: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (selectedRating != 0) {
                    Log.d("rating", "$selectedRating $comment")
                    sharedViewModel.viewModelScope.launch {
                        sharedViewModel.saveRating(selectedRating, comment)
                    }
                } else {
                    sharedViewModel.viewModelScope.launch {
                        sharedViewModel.deleteRating()
                    }
                }

                sharedViewModel.navController?.navigate("movie_page")
            }
        ) {
            Text(
                text = "Save",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
