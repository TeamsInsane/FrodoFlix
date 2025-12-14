package com.frodo.frodoflix.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.viewmodels.SharedViewModel

@Composable
fun JoinGroup(viewModel: SharedViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val allGroups by viewModel.allGroups.collectAsState()

    val navController = viewModel.navController ?: return

    Column(modifier = Modifier.padding(16.dp)) {
        BackToPreviousScreen(navController)
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { 
                searchQuery = it 
                viewModel.searchGroups(it)
            },
            label = { Text("Search for groups") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(allGroups) { group ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(text = group.groupName, modifier = Modifier.weight(1f))
                    Button(onClick = { viewModel.joinGroup(group.groupId) }) {
                        Text("Join")
                    }
                }
            }
        }
    }
}