package com.frodo.frodoflix.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frodo.frodoflix.viewmodels.SharedViewModel

@Composable
fun ChatPage(viewModel: SharedViewModel) {
    val groups by viewModel.groups.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { viewModel.navController?.navigate("join_group") }) {
                Text("Join Groups")
            }
            Button(onClick = { viewModel.navController?.navigate("create_group") }) {
                Text("Create Groups")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Your groups")
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
}
