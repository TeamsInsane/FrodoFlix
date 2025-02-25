package com.frodo.frodoflix.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.viewmodels.GenreList
import com.frodo.frodoflix.viewmodels.SharedViewModel

@Composable
fun FavoriteGenresPage(sharedViewModel: SharedViewModel) {
    val genresViewModel = sharedViewModel.genresViewModel
    val genresUiState by genresViewModel.genresUiState.collectAsState()
    val navController = sharedViewModel.navController ?: return

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                BackToPreviousScreen(navController)

                //Display text
                Text(
                    text = "Select your favorite genres",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp)
                )

                //Display the list of all genres
                GenresList(
                    genreList = genresUiState.genresList,
                    onCheckedChange = { index ->
                        genresViewModel.toggleGenreStatus(index)
                        sharedViewModel.updateUserGenres(genresViewModel.getFavouriteGenresList())
                    }
                )

            }
        }
    }
}

@Composable
fun GenresList(genreList: List<GenreList>, onCheckedChange: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(genreList.size) { index ->
            val genreItem = genreList[index]
            val genre = genreItem.genre
            val isChecked = genreItem.status

            DisplayGenre(genre, isChecked, onCheckedChange = { onCheckedChange(index) })
        }
    }
}

@Composable
fun DisplayGenre(genre: String, isChecked: Boolean, onCheckedChange: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = genre,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )

            Checkbox(
                checked = isChecked,
                onCheckedChange = { onCheckedChange() }
            )
        }
    }
}