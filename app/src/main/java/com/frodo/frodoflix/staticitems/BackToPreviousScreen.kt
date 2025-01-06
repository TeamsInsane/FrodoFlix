package com.frodo.frodoflix.staticitems

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.frodo.frodoflix.R

@SuppressLint("RestrictedApi")
@Composable
fun BackToPreviousScreen(navController : NavController){
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Icon(
            painter = painterResource(id = R.drawable.arrow_back),
            contentDescription = "Back to Home Page",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(48.dp)
                .clickable {
                    Log.d("destination", navController.previousBackStackEntry?.destination?.route.toString())

                    while (navController.previousBackStackEntry?.destination?.route == "rate_movie") {
                        navController.popBackStack()
                        navController.popBackStack()
                    }

                    navController.popBackStack()
                }
        )
    }
}