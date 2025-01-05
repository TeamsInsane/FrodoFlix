package com.frodo.frodoflix.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.viewmodels.SharedViewModel
import kotlin.math.min

@Composable
fun DisplayWantToWatchPage(sharedViewModel: SharedViewModel) {
    val navController = sharedViewModel.navController ?: return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        BackToPreviousScreen(navController)
        DisplayMoviesColumnWTW(sharedViewModel)
        BottomMenuBar(navController)
    }
}

@Composable
fun DisplayMoviesColumnWTW(sharedViewModel: SharedViewModel) {
    LazyColumn {
        val wantToWatchList = sharedViewModel.watchlist.value

        for (i in 0..wantToWatchList.size / 3) {
            val wantToWatchMovieSublist =
                wantToWatchList.subList(i * 3, min(i * 3 + 3, wantToWatchList.size))

            item {
                DisplayListMoviesRow(wantToWatchMovieSublist, sharedViewModel)
            }
        }
    }
}