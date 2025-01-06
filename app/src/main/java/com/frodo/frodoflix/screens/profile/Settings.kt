package com.frodo.frodoflix.screens.profile

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frodo.frodoflix.R
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.viewmodels.SharedViewModel

@Composable
fun SettingsScreen(sharedViewModel: SharedViewModel) {
    val navController = sharedViewModel.navController ?: return

    var isEditingUsername by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf(sharedViewModel.getUsername()) }

    var isEditingPassword by remember { mutableStateOf(false) }
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    Scaffold {innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)
            .padding(20.dp),
        ) {
            BackToPreviousScreen(navController)
            DisplayDarkTheme(sharedViewModel)
            SettingsUsername(sharedViewModel, username, isEditingUsername, onEditClick = { isEditingUsername = !isEditingUsername }, onUsernameChange = { username = it })
            DisplayEmail(sharedViewModel.getEmail())
            SettingsPassword(sharedViewModel, isEditingPassword, oldPassword, newPassword, onEditClick = { isEditingPassword = !isEditingPassword }, onOldPasswordChange = { oldPassword = it }, onNewPasswordChange = { newPassword = it })
            DisplaySignOut(sharedViewModel)
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

    }

    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        thickness = 1.dp,

        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun SettingsUsername(sharedViewModel: SharedViewModel ,username: String, isEditingUsername: Boolean, onEditClick: () -> Unit, onUsernameChange: (String) -> Unit){
    var errorMessage by remember { mutableStateOf("") }

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

        if (isEditingUsername) {

            TextField(
                value = username,
                onValueChange = { onUsernameChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("New Username") },
                singleLine = true,
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Row {

                Button(
                    onClick = {
                        sharedViewModel.changeUsername(username) { result ->
                            if (result) {
                                errorMessage = ""
                                onEditClick()
                            } else {
                                errorMessage = "Failed to change username. Username is already taken."
                            }
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp),

                    ) {
                    Text("Save")
                }

                Button(
                    onClick = {
                        onUsernameChange(sharedViewModel.getUsername())
                        errorMessage = ""
                        onEditClick()
                    },
                    modifier = Modifier.padding(top = 8.dp),

                    ) {
                    Text("Cancel")
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(
                    text = username,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                )
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.pen),
                    contentDescription = "Edit username",
                    tint = MaterialTheme.colorScheme.primary,

                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onEditClick()
                        }
                )
            }
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
fun DisplayEmail(email: String){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)

    ){
        Text(
            text = "Email",
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
                text = email,
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
                    .clickable {
                        Log.d("settings", "can click")
                    }
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
fun SettingsPassword(
    sharedViewModel: SharedViewModel,
    isEditingPassword: Boolean,
    oldPassword: String,
    newPassword: String,
    onEditClick: () -> Unit,
    onOldPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit
) {
    var errorMessage by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = "Password",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
        )

        if (isEditingPassword) {
            // Old password field
            TextField(
                value = oldPassword,
                onValueChange = { onOldPasswordChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Old Password") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
            )

            // New password field
            TextField(
                value = newPassword,
                onValueChange = { onNewPasswordChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("New Password") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Row {
                Button(
                    onClick = {
                        sharedViewModel.changePassword(oldPassword, newPassword) { result ->
                            if (result) {
                                errorMessage = ""
                                onEditClick()
                            } else {
                                errorMessage = "Failed to change password. (Wrong old password)"
                            }
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp),

                    ) {
                    Text("Save")
                }

                Button(
                    onClick = {
                        errorMessage = ""
                        onEditClick()
                    },
                    modifier = Modifier.padding(top = 8.dp),

                    ) {
                    Text("Cancel")
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "*******",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                )
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.pen),
                    contentDescription = "Edit password",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onEditClick()
                        }
                )
            }

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
fun DisplaySignOut(sharedViewModel: SharedViewModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        // Rate movie button
        Button(
            onClick = {
                sharedViewModel.signOut()
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