package com.frodo.frodoflix.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frodo.frodoflix.data.Message
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.viewmodels.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPage(sharedViewModel: SharedViewModel, groupId: String) {
    val messages by sharedViewModel.messages.collectAsState()
    var newMessage by remember { mutableStateOf("") }
    val navController = sharedViewModel.navController ?: return
    val currentUser = sharedViewModel.getUsername()

    LaunchedEffect(groupId) {
        sharedViewModel.listenForMessages(groupId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BackToPreviousScreen(navController)

        LazyColumn(modifier = Modifier
            .weight(1f)
            .padding(horizontal = 8.dp)) {
            items(messages) {
                message ->
                MessageBubble(message = message, isCurrentUser = message.username == currentUser)
            }
        }

        TextField(
            value = newMessage,
            onValueChange = { newMessage = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            placeholder = { Text("Send a message") },
            trailingIcon = {
                IconButton(onClick = {
                    if (newMessage.isNotBlank()) {
                        sharedViewModel.sendMessage(groupId, newMessage)
                        newMessage = ""
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send"
                    )
                }
            }
        )
    }
}

@Composable
fun MessageBubble(message: Message, isCurrentUser: Boolean) {
    val bubbleColor = if (isCurrentUser) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surfaceContainer
    val textColor = if (isCurrentUser) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(bubbleColor)
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = message.username,
                    fontSize = 12.sp,
                    color = textColor
                )
                Text(
                    text = message.content,
                    color = textColor
                )
            }
        }
    }
}
