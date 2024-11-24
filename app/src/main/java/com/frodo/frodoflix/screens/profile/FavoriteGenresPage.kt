package com.frodo.frodoflix.screens.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.frodo.frodoflix.R
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.frodo.frodoflix.api.TMDB
import com.frodo.frodoflix.data.GenreList
import com.frodo.frodoflix.data.GenresViewModel
import org.json.JSONArray

@Composable
fun GenresPage(navController: NavController, genresViewModel: GenresViewModel = viewModel()) {
    val uiState = genresViewModel.uiState.collectAsState()

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
                GoBackToProfile(navController)

                GenresList(uiState.value.genresList) { updatedList ->
                    genresViewModel.updateGenres(updatedList)
                }

                //TODO:Bottom menu
            }
        }
    }
}

@Composable
fun GoBackToProfile(navController: NavController) {
    Image(
        painter = painterResource(id = R.drawable.arrow_back),
        contentDescription = "back",
        modifier = Modifier
            .size(96.dp)
            .padding(bottom = 64.dp, end= 64.dp)
            .clickable {  navController.navigate("profile") }
    )
}

@Composable
fun GenresList(genresList: List<GenreList>, onGenreStatusChange:  (List<GenreList>) -> Unit) {
    var genresList by remember { mutableStateOf<JSONArray?>(null) }

    LaunchedEffect(true) {
        genresList = TMDB.getDataFromTMDB(
            "https://api.themoviedb.org/3/genre/movie/list?language=en",
            "genres"
        )
    }

    DisplayGenresColumn(genresList)
}

@Composable
fun DisplayGenresColumn(genres : JSONArray?) {
    if (genres != null) {
        LazyColumn (
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(genres.length()) { index ->
                val item = genres.getJSONObject(index)
                val id = item.getString("id")
                val genre = item.getString("name")

                DisplayGenre(id, genre)
            }
        }
    } else {
        //TODO:Let the user know
        Log.e("Movies", "Movies is null!")
    }
}

@Composable
fun DisplayGenre(id : String, genre : String){
    var isChecked = remember { mutableStateOf(true) }

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
                checked = isChecked.value,
                onCheckedChange = { isChecked.value = !isChecked.value}
            )
        }
    }
}