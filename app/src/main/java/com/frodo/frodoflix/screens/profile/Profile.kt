package com.frodo.frodoflix.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.frodo.frodoflix.R
import com.frodo.frodoflix.screens.DrawMainPage
import com.frodo.frodoflix.viewmodels.GenresViewModel
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
                DisplayAllMovies()
                DisplayFavouriteMovies()
                DisplayRecentActivity()
                DisplayFavouriteGenres(navController)
            }
            DisplayBottomRow(navController)
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
fun DisplayAllMovies() {
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
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Movies watched: 3018",
                fontSize = 22.sp
            )
        }
    }
}


@Composable
fun DisplayFavouriteMovies() {
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
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Favourite movies",
                fontSize = 22.sp
            )
        }
    }
}


@Composable
fun DisplayRecentActivity() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, bottom = 32.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        // Rate movie button
        Button(
            onClick = {
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Recent activity",
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


@Composable
fun DisplayBottomRow(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxHeight()

    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .clickable {
                    //navController.navigate("favourite_genres")
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {


                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home",
                    tint = MaterialTheme.colorScheme.primary,

                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            navController.navigate("home_page")
                        }
                )
                Icon(
                    painter = painterResource(id = R.drawable.user),
                    tint = MaterialTheme.colorScheme.primary,

                    contentDescription = "User",
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            navController.navigate("login_page")
                        }

                )
            }
        }
    }
}


