package com.frodo.frodoflix.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.frodo.frodoflix.R
import com.frodo.frodoflix.viewmodels.SharedViewModel


@Composable
fun Profile(sharedViewModel: SharedViewModel) {
    val navController = sharedViewModel.navController ?: return

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {
            ProfileTopBar(navController)
            ProfileHeader(sharedViewModel)
            ProfileStats(sharedViewModel)
            Spacer(modifier = Modifier.height(24.dp))
            ProfileActions(sharedViewModel, navController)
        }
    }
}

@Composable
private fun ProfileTopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            painter = painterResource(R.drawable.settings),
            contentDescription = "Settings",
            modifier = Modifier
                .size(28.dp)
                .clickable { navController.navigate("settings") }
        )
    }
}

@Composable
private fun ProfileHeader(sharedViewModel: SharedViewModel) {
    val username = sharedViewModel.getUsername()
    val lastOnline = sharedViewModel.getLastOnlineTime()
    val profileImageUrl = "https://sm.ign.com/ign_ap/review/s/sekiro-sha/sekiro-shadows-die-twice-review_3sf1.jpg"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = profileImageUrl,
            contentDescription = "Profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = username,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 28.sp
        )

        Text(
            text = "Last online • $lastOnline",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun ProfileStats(sharedViewModel: SharedViewModel) {
    val followers = sharedViewModel.getFollowers()
    val following = sharedViewModel.getFollowing()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem("Followers", followers) {
            sharedViewModel.navController?.navigate("followers")
        }
        StatItem("Following", following) {
            sharedViewModel.navController?.navigate("following")
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    count: Int,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = count.toString(),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun ProfileActions(
    sharedViewModel: SharedViewModel,
    navController: NavController
) {
    val favCount = sharedViewModel.favList.collectAsState().value.size
    val watchCount = sharedViewModel.watchlist.collectAsState().value.size
    val watchedCount = sharedViewModel.watchedList.collectAsState().value.size

    // Column with weight spacer to push buttons to bottom
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f)) // pushes buttons down

        ProfileButton("Favourite movies", favCount) {
            navController.navigate("fav_page")
        }

        ProfileButton("Watchlist", watchCount) {
            navController.navigate("watch_list")
        }

        ProfileButton("Watched", watchedCount) {
            navController.navigate("watched_list")
        }

        ProfileButton("Favourite genres") {
            navController.navigate("favourite_genres")
        }

        Spacer(modifier = Modifier.height(24.dp)) // extra space from bottom
    }
}


@Composable
private fun ProfileButton(
    title: String,
    count: Int? = null,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = if (count != null) "$title • $count" else title,
            style = MaterialTheme.typography.bodyLarge
        )
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
fun DisplayUsernameAndOnlineText(sharedViewModel: SharedViewModel) {
    val lastOnlineTime = sharedViewModel.getLastOnlineTime()
    val username = sharedViewModel.getUsername()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = username,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 40.sp,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Last Online: $lastOnlineTime",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 22.sp,
            style = MaterialTheme.typography.bodyLarge
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


//Display watch list button
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

//Display watched list button
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

            val watchedListCount = sharedViewModel.watchedList.collectAsState().value.size

            Text(
                text = "Watched list: $watchedListCount",
                fontSize = 22.sp
            )
        }
    }
}

//Display favorite genres button
@Composable
fun DisplayFavouriteGenres(navController : NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.Center,
    ) {

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