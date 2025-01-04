package com.frodo.frodoflix.screens.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frodo.frodoflix.viewmodels.SharedViewModel
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Base64
import java.security.SecureRandom

@Composable
fun RegisterPage(sharedViewModel: SharedViewModel) {
    var usernameValue by remember { mutableStateOf("") }
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(40.dp),
        ) {
            RegisterText(sharedViewModel)
            RegisterForm(usernameValue, {usernameValue = it}, emailValue, {emailValue = it}, passwordValue, {passwordValue = it})
            DisplayRegister(usernameValue, emailValue, passwordValue, sharedViewModel)
        }
    }
}

@Composable
fun RegisterText(sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier
            .padding(top = 100.dp)
    ) {
        Text(
            text = "Register",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            modifier = Modifier.clickable {
                sharedViewModel.navController?.navigate("login_page")
            },
            text = "Already have an account? Log in",
            fontSize = 16.sp
        )
    }
}

@Composable
fun RegisterForm(
    usernameValue: String,
    onUsernameChange: (String) -> Unit,
    emailValue: String,
    onEmailChange: (String) -> Unit,
    passwordValue: String,
    onPasswordChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 40.dp) // Add top padding for spacing
    ) {
        // Username Address Label
        Text(
            text = "Username",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Username Input Field
        TextField(
            value = usernameValue,
            onValueChange = { onUsernameChange(it) },
            label = { Text("Enter your username") },
            singleLine = true, // Restrict to a single line for email input
            textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Normal),
            modifier = Modifier
                .fillMaxWidth()
        )

        // Email Address Label
        Text(
            text = "Email Address",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp, top = 32.dp)
        )

        // Email Input Field
        TextField(
            value = emailValue,
            onValueChange = { onEmailChange(it) },
            label = { Text("Enter your email") },
            singleLine = true, // Restrict to a single line for email input
            textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Normal),
            modifier = Modifier
                .fillMaxWidth()
        )

        // Email Address Label
        Text(
            text = "Password",

            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp, top = 32.dp)
        )

        // Email Input Field
        TextField(
            value = passwordValue,
            onValueChange = { onPasswordChange(it) },
            label = { Text("Enter your password") },
            singleLine = true, // Restrict to a single line for email input
            textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Normal),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun DisplayRegister(usernameValue: String, emailValue: String, passwordValue: String, sharedViewModel: SharedViewModel){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally    ) {
        Row(
            modifier = Modifier
                .padding(start = 50.dp, end = 50.dp, top = 64.dp, bottom = 64.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(6.dp)
                .clickable  {
                    val (hashedPassword, salt) = sharedViewModel.hashPassword(passwordValue)
                    sharedViewModel.newUser(usernameValue, emailValue, hashedPassword, salt)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center

        ) {

            Text(
                text = "Sign up",
                fontSize = 18.sp,
            )
        }

    }
}