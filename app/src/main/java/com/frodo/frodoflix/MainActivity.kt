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
import androidx.room.Room
import com.frodo.frodoflix.database.FrodoDatabase
import com.frodo.frodoflix.screens.DisplayMoviePage
import com.frodo.frodoflix.screens.RateMovie
import com.frodo.frodoflix.screens.registration.LoginPage
import com.frodo.frodoflix.screens.profile.SettingsScreen
import com.frodo.frodoflix.screens.registration.RegisterPage
import com.frodo.frodoflix.viewmodels.GenresViewModel
import com.frodo.frodoflix.viewmodels.SharedViewModel

class MainActivity : ComponentActivity() {
    /*
    companion object {
        lateinit var frodoDatabase: FrodoDatabase
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*frodoDatabase = Room.databaseBuilder(
            applicationContext,
            FrodoDatabase::class.java,
            FrodoDatabase.NAME
        ).build()*/

        enableEdgeToEdge()
        val genresViewModel: GenresViewModel = ViewModelProvider(this)[GenresViewModel::class.java]

        setContent {
            val sharedViewModel: SharedViewModel = viewModel()
            val navController = rememberNavController()
            sharedViewModel.navController = navController
            //sharedViewModel.frodoDao = frodoDatabase.frodoDao();

            FrodoFlixTheme (darkTheme = sharedViewModel.isDarkTheme.value){
                NavHost(navController = navController, startDestination = "home_page") {
                    // First screen (Home Page)
                    composable("home_page") {
                        DrawMainPage(sharedViewModel)
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
                        FavoriteGenresPage(sharedViewModel, genresViewModel)
                    }

                    // Movie page
                    composable("movie_page") {
                        DisplayMoviePage(sharedViewModel)
                    }

                    // Rate movie
                    composable("rate_movie"){
                        RateMovie(sharedViewModel)
                    }
                }
            }
        }
    }
}