package com.frodo.frodoflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frodo.frodoflix.screens.profile.Genres
import com.frodo.frodoflix.screens.profile.Profile
import com.frodo.frodoflix.screens.DrawMainPage
import com.frodo.frodoflix.ui.theme.FrodoFlixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FrodoFlixTheme {
                // Set up navigation
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home_page") {
                    // First screen (MainScreen)
                    composable("home_page") {
                        DrawMainPage(navController)
                    }
                    // Second screen (NewScreen)
                    composable("profile") {
                        Profile(navController) // Use the Profile composable here
                    }
                    // Favourite genres
                    composable("favourite_genres"){
                        Genres(navController)
                    }
                }
            }
        }
    }
}