package com.frodo.frodoflix.screens.profile

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.frodo.frodoflix.R
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.viewmodels.SharedViewModel

@Composable
fun SettingsScreen(sharedViewModel: SharedViewModel) {
    val navController = sharedViewModel.navController ?: return

    Scaffold {innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)
            .padding(20.dp),
        ) {
            BackToPreviousScreen(navController)
            DisplayDarkTheme(sharedViewModel)
            SettingsUsername()
            DisplayEmail()
            DisplayChangePassword()
            DisplaySignOut()
        }
    }
}


@Composable
fun DisplayDarkTheme(sharedViewModel: SharedViewModel){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Dark Theme",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,

                )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = sharedViewModel.isDarkTheme.value,
                onCheckedChange = {
                    sharedViewModel.toggleTheme()
                },
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            thickness = 1.dp,

            color = MaterialTheme.colorScheme.onSurface
        )
    }

}

@Composable
fun SettingsUsername(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)

    ){
        Text(
            text = "Username",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                text = "Frodo",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.pen),
                contentDescription = "Back to Profile",
                tint = MaterialTheme.colorScheme.primary,

                modifier = Modifier
                    .size(24.dp)
            )


        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            thickness = 1.dp,

            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun DisplayEmail(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)

    ){
        Text(
            text = "Email address",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                text = "Frodo.Baggins@gmail.com",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.pen),
                contentDescription = "Back to Profile",
                tint = MaterialTheme.colorScheme.primary,

                modifier = Modifier
                    .size(24.dp)
            )


        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            thickness = 1.dp,

            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun DisplayChangePassword(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)

    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                text = "Change password",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.pen),
                contentDescription = "Back to Profile",
                tint = MaterialTheme.colorScheme.primary,

                modifier = Modifier
                    .size(24.dp)
            )

        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 400.dp),
            thickness = 1.dp,

            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun DisplaySignOut(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        // Rate movie button
        Button(
            onClick = {
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Sign out",
                fontSize = 22.sp
            )
        }
    }
}