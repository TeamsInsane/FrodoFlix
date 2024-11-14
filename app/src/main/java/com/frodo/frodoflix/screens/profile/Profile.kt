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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.frodo.frodoflix.R

@Composable
fun Profile(navController: NavController) {
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
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.settings),
                        contentDescription = "Settings",
                        modifier = Modifier.size(32.dp)
                    )
                }

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

                // Username
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 48.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Frodo",
                        fontSize = 40.sp,
                    )
                }


                // All movies
                Box(
                    modifier = Modifier
                        .padding(start = 50.dp, end = 50.dp, top = 8.dp, bottom = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
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

                // Favourite movies
                Box(
                    modifier = Modifier
                        .padding(start = 50.dp, end = 50.dp, top = 8.dp, bottom = 8.dp)
                        .clip(RoundedCornerShape(12.dp)) // Apply rounded corners
                        .background(MaterialTheme.colorScheme.surfaceVariant) // Set background color
                        .padding(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center

                    ) {
                        Image(
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

                // Recent activity
                Box(
                    modifier = Modifier
                        .padding(start = 50.dp, end = 50.dp, top = 8.dp, bottom = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.tv),
                            contentDescription = "Profile Picture",
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

                // Advanced statistics
                Box(
                    modifier = Modifier
                        .padding(start = 50.dp, end = 50.dp, top = 8.dp, bottom = 64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(6.dp)

                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
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


                // Select genres you want to watch
                Box(
                    modifier = Modifier
                        .padding(start = 50.dp, end = 50.dp, top = 8.dp, bottom = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
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
                        Image(
                            painter = painterResource(id = R.drawable.eye),
                            contentDescription = "Profile Picture",
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






                // Spacer(modifier = Modifier.weight(1f))



                // Bottom Buttons
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { navController.navigate("home_page") }
                    ) {
                        Text(text = "Home Screen")
                    }
                    Button(
                        onClick = { /* TODO: Another action */ }
                    ) {
                        Text(text = "Click Me Too")
                    }
                }
            }
        }
    }
}
