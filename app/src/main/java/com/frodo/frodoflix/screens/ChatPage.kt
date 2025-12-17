package com.frodo.frodoflix.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.viewmodels.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPage(sharedViewModel: SharedViewModel, groupId: String) {
    val messages by sharedViewModel.messages.collectAsState()
    var newMessage by remember { mutableStateOf("") }
    val navController = sharedViewModel.navController ?: return

    LaunchedEffect(groupId) {
        sharedViewModel.listenForMessages(groupId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BackToPreviousScreen(navController)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) {
                message ->
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "${message.username}: ${message.content}")
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = newMessage,
                onValueChange = { newMessage = it },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { sharedViewModel.sendMessage(groupId, newMessage); newMessage = "" }) {
                Text("Send")
            }
        }
    }
}
