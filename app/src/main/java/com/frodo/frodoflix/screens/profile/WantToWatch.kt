package com.frodo.frodoflix.screens.profile

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.frodo.frodoflix.api.TMDB
import com.frodo.frodoflix.data.Movie
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.viewmodels.SharedViewModel
import org.json.JSONObject
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
                DisplayMoviesRow(wantToWatchMovieSublist, sharedViewModel)
            }
        }
    }
}