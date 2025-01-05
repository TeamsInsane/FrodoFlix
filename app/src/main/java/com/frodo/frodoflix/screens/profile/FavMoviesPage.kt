package com.frodo.frodoflix.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.viewmodels.SharedViewModel
import kotlin.math.min
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun DisplayFavMoviesPage(sharedViewModel: SharedViewModel) {
    val navController = sharedViewModel.navController ?: return

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            item {
                BackToPreviousScreen(navController)

                // Favorite movie text
                Text(
                    text = "Favourite movies",
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp),
                    fontWeight = FontWeight.Bold
                )

                DisplayMoviesColumnFavList(sharedViewModel)
            }
        }

        BottomMenuBar(navController)
    }
}

@Composable
fun DisplayMoviesColumnFavList(sharedViewModel: SharedViewModel) {
    val favList = sharedViewModel.favList.collectAsState().value

    for (i in 0..favList.size / 3) {
        val wantToWatchMovieSublist =
            favList.subList(i * 3, min(i * 3 + 3, favList.size))


            DisplayListMoviesRow(wantToWatchMovieSublist, sharedViewModel)

    }
}