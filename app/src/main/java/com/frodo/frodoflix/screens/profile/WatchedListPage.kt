package com.frodo.frodoflix.screens.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.viewmodels.SharedViewModel
import kotlin.math.min

@Composable
fun DisplayWatchedPage(sharedViewModel: SharedViewModel) {
    val navController = sharedViewModel.navController ?: return

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            item {
                BackToPreviousScreen(navController)

                // Watchedlist movies text
                Text(
                    text = "Watched Movies",
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp),
                    fontWeight = FontWeight.Bold
                )

                DisplayMoviesColumnW(sharedViewModel)
            }
        }

        BottomMenuBar(navController)
    }
}

@Composable
fun DisplayMoviesColumnW(sharedViewModel: SharedViewModel) {
    val watchedList = sharedViewModel.watchedList.collectAsState().value

    for (i in 0..watchedList.size / 3) {
        val watchedSubList =
            watchedList.subList(i * 3, min(i * 3 + 3, watchedList.size))

        DisplayListMoviesRow(watchedSubList, sharedViewModel)
    }
}

