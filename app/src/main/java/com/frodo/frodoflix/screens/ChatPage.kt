package com.frodo.frodoflix.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frodo.frodoflix.data.Group
import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.viewmodels.SharedViewModel

@Composable
fun ChatPage(viewModel: SharedViewModel) {
    val navController = viewModel.navController ?: return

    Scaffold {innerPadding ->
        var createGroupId by remember { mutableStateOf("") }
        var createGroupName by remember { mutableStateOf("") }
        var joinGroupId by remember { mutableStateOf("") }
        val groups by viewModel.groups.collectAsState()

        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            // Create Group
            OutlinedTextField(
                value = createGroupId,
                onValueChange = { createGroupId = it },
                label = { Text("Group ID") }
            )
            OutlinedTextField(
                value = createGroupName,
                onValueChange = { createGroupName = it },
                label = { Text("Group Name") }
            )
            Button(onClick = { viewModel.createGroup(createGroupId, createGroupName) }) {
                Text("Create Group")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Join Group
            OutlinedTextField(
                value = joinGroupId,
                onValueChange = { joinGroupId = it },
                label = { Text("Group ID") }
            )
            Button(onClick = { viewModel.joinGroup(joinGroupId) }) {
                Text("Join Group")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Group List
            LazyColumn {
                items(groups) { group ->
                    Row {
                        Text(text = group.groupName)
                        Button(onClick = { /* TODO: Navigate to chat */ }) {
                            Text("Open Chat")
                        }
                    }
                }
            }
        }

        BottomMenuBar(navController)
    }
}