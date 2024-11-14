package com.frodo.frodoflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        fontSize = 36.sp, //scalable pix
        modifier = modifier
    )
    Text(
        text = "abc",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FrodoFlixTheme {
        Greeting("Android")
    }
}