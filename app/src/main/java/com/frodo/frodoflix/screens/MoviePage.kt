package com.frodo.frodoflix.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.frodo.frodoflix.data.Movie

@Composable
fun DisplayMoviePage(navController: NavController, movie: Movie) {
    Text(
        text = movie.title + "\n" + movie.overview,
        fontSize = 16.sp,
        modifier = Modifier.padding(top = 8.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )

}