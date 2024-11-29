package com.frodo.frodoflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.frodo.frodoflix.screens.profile.Profile
import com.frodo.frodoflix.screens.DrawMainPage
import com.frodo.frodoflix.screens.profile.FavoriteGenresPage
import com.frodo.frodoflix.ui.theme.FrodoFlixTheme
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.frodo.frodoflix.screens.DisplayMoviePage
import com.frodo.frodoflix.viewmodels.GenresViewModel
import com.frodo.frodoflix.viewmodels.SharedViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val genresViewModel: GenresViewModel = ViewModelProvider(this)[GenresViewModel::class.java]
        setContent {
            val sharedViewModel: SharedViewModel = viewModel()
            val navController = rememberNavController()
            sharedViewModel.navController = navController

            FrodoFlixTheme {
                NavHost(navController = navController, startDestination = "home_page") {
                    // First screen (Home Page)
                    composable("home_page") {
                        DrawMainPage(sharedViewModel)
                    }
                    // Profile Page
                    composable("profile") {
                        Profile(sharedViewModel)
                    }

                    // Favourite genres
                    composable("favourite_genres") {
                        FavoriteGenresPage(sharedViewModel, genresViewModel)
                    }

                    // Movie page
                    composable("movie_page") {
                        DisplayMoviePage(sharedViewModel)
                    }
                }
            }
        }
    }
}