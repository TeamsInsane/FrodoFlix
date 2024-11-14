package com.frodo.frodoflix.screens.profile

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
import androidx.compose.foundation.shape.RoundedCornerShape
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


@Composable
fun Genres(navController: NavController) {
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
                // Top Row with Settings Icon
                Row(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 64.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = "back",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                navController.navigate("profile")
                            }
                    )
                }

                val genres =
                    listOf("Animation", "Action", "Drama", "Comedy", "Horror") // List of genres
                val checkedStates =
                    remember { mutableStateOf(genres.map { true }) } // List of boolean values for each genre, starting with `true` (checked)

                Column {
                    genres.forEachIndexed { index, genre ->
                        val isChecked = checkedStates.value[index]

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
                                    text = genre, // Display the genre name
                                    fontSize = 18.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                // Clickable Box for each genre
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clickable {
                                            // Toggle the checked state of the corresponding genre
                                            checkedStates.value =
                                                checkedStates.value.toMutableList().apply {
                                                    this[index] =
                                                        !this[index] // Toggle the state for this specific genre
                                                }
                                        }
                                ) {
                                    // Display checkmark or unchecked based on the state
                                    if (isChecked) {
                                        Image(
                                            painter = painterResource(id = R.drawable.check),
                                            contentDescription = "Checkmark",
                                            modifier = Modifier.size(32.dp)
                                        )
                                    } else {
                                        Image(
                                            painter = painterResource(id = R.drawable.unchecked),
                                            contentDescription = "Unchecked",
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
