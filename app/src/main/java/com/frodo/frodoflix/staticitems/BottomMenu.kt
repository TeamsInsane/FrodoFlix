package com.frodo.frodoflix.staticitems

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.frodo.frodoflix.R

@Composable
fun BottomMenuBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(80.dp)
                .padding(12.dp),
            shape = RoundedCornerShape(40.dp),
            color = MaterialTheme.colorScheme.surfaceDim
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

                BottomMenuItem(
                    iconRes = R.drawable.home,
                    contentDescription = "Home",
                    isSelected = currentDestination == "home_page",
                    onClick = { navController.navigate("home_page") }
                )

                BottomMenuItem(
                    iconRes = R.drawable.search,
                    contentDescription = "Search",
                    isSelected = currentDestination == "search_page",
                    onClick = { navController.navigate("search_page") }
                )

                BottomMenuItem(
                    iconRes = R.drawable.pen,
                    contentDescription = "Chat",
                    isSelected = currentDestination == "chat_page",
                    onClick = { navController.navigate("chat_page") }
                )

                BottomMenuItem(
                    iconRes = R.drawable.user,
                    contentDescription = "Profile",
                    isSelected = currentDestination == "profile",
                    onClick = { navController.navigate("profile") }
                )
            }
        }
    }
}

@Composable
fun BottomMenuItem(
    iconRes: Int,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = CircleShape
                )
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription,
                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
