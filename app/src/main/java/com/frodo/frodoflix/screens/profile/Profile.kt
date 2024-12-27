package com.frodo.frodoflix.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.frodo.frodoflix.R
import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.viewmodels.SharedViewModel


@Composable
fun Profile(sharedViewModel: SharedViewModel) {
    val navController = sharedViewModel.navController ?: return

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background_image), // Replace with your image resource
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize()
        )

        // Foreground Content
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f) // Optional: Adds slight transparency
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp),
            ) {
                DisplaySettingsIcon(navController)
                DisplayProfileIcon()
                DisplayUsername()
                DisplayAllMovies(sharedViewModel, navController)
                DisplayWatchList(navController)
                DisplayFavouriteGenres(navController)
            }
            BottomMenuBar(navController)
        }
    }
}


@Composable
fun DisplaySettingsIcon(navController: NavController) {
    // Top Row with Settings Icon
    Row(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 64.dp)
            .fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.settings),
            contentDescription = "Settings",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(32.dp)
                .clickable {
                    navController.navigate("settings")
                }
        )
    }
}

@Composable
fun DisplayProfileIcon() {

    // Profile Picture
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.frodo),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun DisplayUsername() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 48.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Frodo",
            color = MaterialTheme.colorScheme.onSurface,

            fontSize = 40.sp,
        )
    }
}


@Composable
fun DisplayAllMovies(sharedViewModel: SharedViewModel, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        // Rate movie button
        Button(
            onClick = {
                navController.navigate("movies_watched")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {

            val moviesWatchCount = sharedViewModel.watchedlist.collectAsState().value.size

            Text(
                text = "Movies watched: $moviesWatchCount",
                fontSize = 22.sp
            )
        }
    }
}


@Composable
fun DisplayWatchList(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 32.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        // Rate movie button
        Button(
            onClick = {
                navController.navigate("want_to_watch")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Watch list",
                fontSize = 22.sp
            )
        }
    }
}


@Composable
fun DisplayFavouriteGenres(navController : NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        // Rate movie button
        Button(
            onClick = {
                navController.navigate("favourite_genres")

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Favourite genres",
                fontSize = 22.sp
            )
        }
    }
}