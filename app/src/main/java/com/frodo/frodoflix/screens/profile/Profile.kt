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

    Scaffold {innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            DisplaySettingsIcon(navController)
            DisplayProfileIcon()
            DisplayUsername()
            DisplayAllMovies()
            DisplayFavouriteMovies()
            DisplayRecentActivity()
            //DisplayAdvancedStatistics()
            DisplayFavouriteGenres(navController)
        }
        DisplayBottomRow(navController)

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
    Box(
        modifier = Modifier
            .padding(start = 50.dp, end = 50.dp, top = 8.dp, bottom = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(6.dp)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Movies watched: ",
                fontSize = 18.sp,
            )
            Text(
                text = "3018",
                fontSize = 24.sp,
            )
        }
    }
}


@Composable
fun DisplayFavouriteMovies() {
    Box(
        modifier = Modifier
            .padding(start = 50.dp, end = 50.dp, top = 8.dp, bottom = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center

        ) {
            Icon(
                painter = painterResource(id = R.drawable.favourite_movies),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "Favourite movies",
                fontSize = 18.sp,
            )
        }
    }
}


@Composable
fun DisplayRecentActivity() {
    Box(
        modifier = Modifier
            .padding(start = 50.dp, end = 50.dp, top = 8.dp, bottom = 64.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center

        ) {
            Icon(
                painter = painterResource(id = R.drawable.tv),
                contentDescription = "Profile Picture",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "Recent activity",
                fontSize = 18.sp,
            )
        }
    }

}


@Composable
fun DisplayAdvancedStatistics() {
    Box(
        modifier = Modifier
            .padding(start = 50.dp, end = 50.dp, top = 8.dp, bottom = 64.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(6.dp)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.advanced_statistics),
                contentDescription = "statistics Picture",
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "Advanced statistics",
                fontSize = 18.sp
            )
        }
    }
}


@Composable
fun DisplayFavouriteGenres(navController : NavController) {
    Box(
        modifier = Modifier
            .padding(start = 50.dp, end = 50.dp, top = 8.dp, bottom = 130.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(6.dp)
            .clickable {
                navController.navigate("favourite_genres")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,

            ) {
            Icon(
                painter = painterResource(id = R.drawable.eye),
                contentDescription = "Favourite genres",
                tint = MaterialTheme.colorScheme.primary,

                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "Favourite genres",
                fontSize = 18.sp,
            )

        }
    }
}


@Composable
fun DisplayBottomRow(navController : NavController) {
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
        ){
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

