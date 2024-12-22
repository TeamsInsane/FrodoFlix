package com.frodo.frodoflix.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.frodo.frodoflix.R

import com.frodo.frodoflix.viewmodels.SharedViewModel

@Composable
fun RateMovie(sharedViewModel: SharedViewModel){
    val navController = sharedViewModel.navController ?: return
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp),
        ) {
            DisplayBackToMoviePage(navController)
            DisplayReview()
            DisplaySaveReview()
        }
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
            modifier = Modifier.size(32.dp)
                .clickable {
                    navController.navigate("movie_page")
                }
        )
    }
}

@Composable
fun DisplayReview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .width(300.dp)
    ) {
        TextField(
            value = "",
            onValueChange = {},
            label = { Text("Add a review...") },
            modifier = Modifier
                .fillMaxWidth() // Takes up the full width
                .weight(0.5f)  // Takes up half of the available height
                .padding(16.dp) // Add some padding around the text field
        )

    }
}

@Composable
fun DisplaySaveReview() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        // Rate movie button
        Button(
            onClick = {
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