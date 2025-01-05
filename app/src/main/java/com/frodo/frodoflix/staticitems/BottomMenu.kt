package com.frodo.frodoflix.staticitems

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.frodo.frodoflix.R

@Composable
fun BottomMenuBar(navController : NavController) {
    Column(
        modifier = Modifier
            .fillMaxHeight()

    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HomePage(navController)
                SearchPage(navController)
                ProfilePage(navController)

            }
        }
    }
}

@Composable
fun HomePage(navController: NavController){
    Icon(
        painter = painterResource(id = R.drawable.home),
        contentDescription = "Home",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(48.dp)
            .padding(10.dp)
            .clickable {
                navController.navigate("home_page")
            }
    )
}

@Composable
fun SearchPage(navController: NavController){
    Icon(
        painter = painterResource(id = R.drawable.search),
        contentDescription = "Search",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(48.dp)
            .padding(10.dp)
            .clickable {
                navController.navigate("search_page")
            }
    )
}

@Composable
fun ProfilePage(navController: NavController){

    Icon(
        painter = painterResource(id = R.drawable.user),
        contentDescription = "User",
        tint = MaterialTheme.colorScheme.primary,

        modifier = Modifier
            .size(48.dp)
            .clickable {
                navController.navigate("profile")
            }
            .padding(10.dp)
    )
}