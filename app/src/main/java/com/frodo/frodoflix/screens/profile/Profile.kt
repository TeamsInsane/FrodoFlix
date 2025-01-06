package com.frodo.frodoflix.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.25f)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp),
            ) {
                DisplaySettingsIcon(navController)
                DisplayProfileIcon()
                DisplayUsername(sharedViewModel.getUsername())
                DisplayFavMoviesButton(sharedViewModel, navController)
                DisplayWatchList(sharedViewModel, navController)
                DisplayWatchedList(sharedViewModel, navController)
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
            .padding(top = 16.dp)
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
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
    }
}

@Composable
fun DisplayUsername(username: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 100.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = username,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 40.sp,
            style = MaterialTheme.typography.titleMedium
        )
    }
}


@Composable
fun DisplayFavMoviesButton(sharedViewModel: SharedViewModel, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        //Favourite movies button
        Button(
            onClick = {
                navController.navigate("fav_page")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceDim,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .height(50.dp)
                .clip(MaterialTheme.shapes.medium),
            contentPadding = PaddingValues(0.dp)
        ) {

            val favMoviesCount = sharedViewModel.favList.collectAsState().value.size

            Text(
                text = "Favourite movies: $favMoviesCount",
                fontSize = 22.sp,
            )
        }
    }
}


@Composable
fun DisplayWatchList(sharedViewModel: SharedViewModel, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.Center,
    ) {

        Button(
            onClick = {
                navController.navigate("watch_list")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceDim,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .height(50.dp)
                .clip(MaterialTheme.shapes.medium),
            contentPadding = PaddingValues(0.dp)
        ) {

            val watchListCount = sharedViewModel.watchlist.collectAsState().value.size

            Text(
                text = "Watch list: $watchListCount",
                fontSize = 22.sp
            )
        }
    }
}

@Composable
fun DisplayWatchedList(sharedViewModel: SharedViewModel, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 32.dp),
        horizontalArrangement = Arrangement.Center,
    ) {

        Button(
            onClick = {
                navController.navigate("watched_list")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceDim,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .height(50.dp)
                .clip(MaterialTheme.shapes.medium),
            contentPadding = PaddingValues(0.dp)
        ) {

            val watchListCount = sharedViewModel.watchedList.collectAsState().value.size

            Text(
                text = "Watch list: $watchListCount",
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
                .fillMaxWidth()
                .height(50.dp)
                .clip(MaterialTheme.shapes.medium),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Favourite genres",
                fontSize = 22.sp
            )
        }
    }
}