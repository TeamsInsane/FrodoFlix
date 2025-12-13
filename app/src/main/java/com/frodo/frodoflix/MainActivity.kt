package com.frodo.frodoflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.frodo.frodoflix.screens.ChatPage
import com.frodo.frodoflix.screens.DisplayMoviePage
import com.frodo.frodoflix.screens.HomePage
import com.frodo.frodoflix.screens.RateMovie
import com.frodo.frodoflix.screens.SearchPage
import com.frodo.frodoflix.screens.profile.DisplayFavMoviesPage
import com.frodo.frodoflix.screens.profile.DisplayWantToWatchPage
import com.frodo.frodoflix.screens.profile.DisplayWatchedPage
import com.frodo.frodoflix.screens.profile.FavoriteGenresPage
import com.frodo.frodoflix.screens.profile.Profile
import com.frodo.frodoflix.screens.profile.SettingsScreen
import com.frodo.frodoflix.screens.registration.LoginPage
import com.frodo.frodoflix.screens.registration.RegisterPage
import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.ui.theme.FrodoFlixTheme
import com.frodo.frodoflix.viewmodels.LifecycleViewModel
import com.frodo.frodoflix.viewmodels.SharedViewModel


class MainActivity : ComponentActivity() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var navController : NavHostController
    private val lifecycleViewModel: LifecycleViewModel by viewModels()
    private var hasBeenPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val appStatus = lifecycleViewModel.appStatus.collectAsState()
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(appStatus.value) {
                if (appStatus.value == "Resumed" && hasBeenPaused) {
                    snackbarHostState.showSnackbar("Welcome back to FrodoFlix!")
                }
            }

            sharedViewModel = viewModel()
            sharedViewModel.initializeGenresViewModel(this)
            navController = rememberNavController()
            sharedViewModel.navController = navController
            sharedViewModel.sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE)

            FrodoFlixTheme(darkTheme = sharedViewModel.isDarkTheme.value) {
                Scaffold(
                    snackbarHost = {
                        Column (
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SnackbarHost(snackbarHostState)
                        }
                    },
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        if (currentRoute in listOf("home_page", "search_page", "chat_page", "profile")) {
                            BottomMenuBar(navController)
                        }
                    },
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login_page",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // First screen (Home Page)
                        composable("home_page") {
                            HomePage(sharedViewModel )
                        }

                        // Search page
                        composable("search_page") {
                            SearchPage(sharedViewModel)
                        }

                        // Chat page
                        composable("chat_page") {
                            ChatPage(sharedViewModel)
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
                        composable("settings") {
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
                        composable("rate_movie") {
                            RateMovie(sharedViewModel)
                        }

                        // Watch list page
                        composable("watch_list") {
                            DisplayWantToWatchPage(sharedViewModel)
                        }

                        // Fav movies list page
                        composable("fav_page") {
                            DisplayFavMoviesPage(sharedViewModel)
                        }

                        // Watched list page
                        composable("watched_list") {
                            DisplayWatchedPage(sharedViewModel)
                        }
                    }
                }

                //Fix za dark theme switch
                if (!sharedViewModel.isUserSet()) {
                    sharedViewModel.checkSavedLogin { result ->
                        if (result) {
                            navController.navigate("home_page")
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleViewModel.setAppStatus("Resumed")
    }


    override fun onStart() {
        super.onStart()
        lifecycleViewModel.setAppStatus("Resumed")
    }

    override fun onPause() {
        super.onPause()
        hasBeenPaused = true
        lifecycleViewModel.setAppStatus("Paused")

        val currentTime = System.currentTimeMillis()
        this.sharedViewModel.saveLastOnlineTime(currentTime)
    }

    override fun onStop() {
        super.onStop()
        hasBeenPaused = true
        lifecycleViewModel.setAppStatus("Paused")

        val currentTime = System.currentTimeMillis()
        this.sharedViewModel.saveLastOnlineTime(currentTime)
    }
}