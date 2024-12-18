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
import androidx.compose.material3.Divider
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
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.frodo.frodoflix.R
import com.frodo.frodoflix.viewmodels.SharedViewModel


@Preview
@Composable
fun SettingsScreen() {
    //val navController = sharedViewModel.navController ?: return

    Scaffold {innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            BackToProfile()
            DisplayDarkTheme()
            SettingsUsername()
            DisplayEmail()
            DisplayChangePassword()
            DisplaySignOut()
        }
    }
}

@Composable
fun BackToProfile(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        Icon(
            painter = painterResource(id = R.drawable.arrow_back),
            contentDescription = "Back to Profile",
            tint = MaterialTheme.colorScheme.primary,

            modifier = Modifier
                .size(48.dp)
                .clickable {
                }
        )
    }
}

@Composable
fun DisplayDarkTheme(){
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
                checked = true,
                onCheckedChange = { },
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
                .padding(top = 16.dp, bottom = 16.dp),
            thickness = 1.dp,

            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun DisplaySignOut(){
    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .padding(start = 50.dp, end = 50.dp, top = 8.dp, bottom = 64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Red)
                .padding(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {

                Text(
                    text = "Sign out",
                    fontSize = 18.sp,
                )
            }
        }
    }
}