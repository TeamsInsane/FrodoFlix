package com.frodo.frodoflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.frodo.frodoflix.screens.profile.Profile
import com.frodo.frodoflix.screens.DrawMainPage
import com.frodo.frodoflix.screens.profile.FavoriteGenresPage
import com.frodo.frodoflix.ui.theme.FrodoFlixTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.frodo.frodoflix.screens.DisplayMoviePage
import com.frodo.frodoflix.screens.RateMovie
import com.frodo.frodoflix.screens.SearchPage
import com.frodo.frodoflix.screens.profile.DisplayWantToWatchPage
import com.frodo.frodoflix.screens.profile.DisplayWatchedListPage
import com.frodo.frodoflix.screens.registration.LoginPage
import com.frodo.frodoflix.screens.profile.SettingsScreen
import com.frodo.frodoflix.screens.registration.RegisterPage
import com.frodo.frodoflix.viewmodels.SharedViewModel

class MainActivity : ComponentActivity() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var navController : NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            sharedViewModel = viewModel()
            sharedViewModel.initializeGenresViewModel(this)
            navController = rememberNavController()
            sharedViewModel.navController = navController

            FrodoFlixTheme (darkTheme = sharedViewModel.isDarkTheme.value){

                NavHost(navController = navController, startDestination = "login_page") {
                    // First screen (Home Page)
                    composable("home_page") {
                        DrawMainPage(sharedViewModel)
                    }

                    // Search page
                    composable("search_page"){
                        SearchPage(sharedViewModel)
                    }

                    // Profile Page
                    composable("profile") {
                        Profile(sharedViewModel)
                    }

                    //Login page
                    composable("login_page") {
                        LoginPage(sharedViewModel)
                    }

                    //Register page
                    composable("register_page") {
                        RegisterPage(sharedViewModel)
                    }

                    // Settings
                    composable("settings"){
                        SettingsScreen(sharedViewModel)
                    }

                    // Favourite genres
                    composable("favourite_genres") {
                        FavoriteGenresPage(sharedViewModel)
                    }

                    // Movie page
                    composable("movie_page") {
                        DisplayMoviePage(sharedViewModel)
                    }

                    // Rate movie
                    composable("rate_movie"){
                        RateMovie(sharedViewModel)
                    }

                    // Watched movies
                    composable("movies_watched"){
                        DisplayWatchedListPage(sharedViewModel)
                    }

                    // want to watch
                    composable("want_to_watch"){
                        DisplayWantToWatchPage(sharedViewModel)
                    }
                }
            }
        }
    }
}