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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.frodo.frodoflix.R
import com.frodo.frodoflix.data.User
import com.frodo.frodoflix.staticitems.BottomMenuBar

import com.frodo.frodoflix.viewmodels.SharedViewModel
import kotlinx.coroutines.launch

@Composable
fun RateMovie(sharedViewModel: SharedViewModel){
    val navController = sharedViewModel.navController ?: return
    var selectedRating by remember { mutableIntStateOf(0) }
    var writtenComment by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp),
        ) {
            DisplayBackToMoviePage(navController)
            DisplayRatingDropdown { rating ->
                if (rating != null) {
                    selectedRating = rating
                }
            }
            DisplayReview { comment ->
                if (comment != null) {
                    writtenComment = comment
                }
            }
            DisplaySaveReview(sharedViewModel, selectedRating, writtenComment)
        }

        BottomMenuBar(navController)
    }
}

@Composable
fun DisplayBackToMoviePage(navController: NavController){
    Row(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 64.dp)
            .fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.close),
            contentDescription = "Settings",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    navController.navigate("movie_page")
                }
        )
    }
}

@Composable
fun DisplayReview(onResult: (String?) -> Unit) {
    var comment by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .width(300.dp)
    ) {
        TextField(
            value = comment,
            onValueChange = {
                comment = it
                onResult(comment )
            },
            label = { Text("Add a review...") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
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
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(8.dp)
                .height(56.dp)
        ) {
            Text(
                text = "Rating: $selectedRating",
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                (1..5).forEach { rating ->
                    DropdownMenuItem(
                        text = { Text(text = "⭐ $rating") },
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = {
                if (selectedRating != 0) {
                    Log.d("rating", "$selectedRating $comment")
                    sharedViewModel.viewModelScope.launch {
                        sharedViewModel.saveRating(selectedRating, comment)

                    }
                }

                val movieID = sharedViewModel.selectedMovie!!.id
                sharedViewModel.updateWatchedlist(movieID = movieID)
                sharedViewModel.navController?.navigate("movie_page")

            },

            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green,
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Save",
                fontSize = 22.sp
            )
        }
    }
}