package com.frodo.frodoflix.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.frodo.frodoflix.R

import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.viewmodels.SharedViewModel


@Composable
fun SearchPage(sharedViewModel: SharedViewModel){
    val navController = sharedViewModel.navController ?: return

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp),
        ) {
            DisplaySearch()
        }
        BottomMenuBar(navController)
    }
}

@Composable
fun DisplaySearch() {
    var movieName by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = movieName,
            onValueChange = { movieName = it },
            label = { Text("Name of the movie ...") },
            singleLine = true, // Prevents multiline text
            shape = RoundedCornerShape(16.dp), // Rounded corners
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        Icon(
            painter = painterResource(id = R.drawable.search),
            contentDescription = "Search",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    // TODO: Perform search
                }
        )
    }
}