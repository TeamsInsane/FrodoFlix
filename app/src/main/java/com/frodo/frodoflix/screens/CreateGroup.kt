package com.frodo.frodoflix.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.viewmodels.SharedViewModel

@Composable
fun CreateGroup(viewModel: SharedViewModel) {
    var groupId by remember { mutableStateOf("") }
    var groupName by remember { mutableStateOf("") }
    var groupDescription by remember { mutableStateOf("") }
    val navController = viewModel.navController ?: return

    Column {
        BackToPreviousScreen(navController)
        OutlinedTextField(
            value = groupId,
            onValueChange = { groupId = it },
            label = { Text("Group ID") }
        )
        OutlinedTextField(
            value = groupName,
            onValueChange = { groupName = it },
            label = { Text("Group Name") }
        )
        OutlinedTextField(
            value = groupDescription,
            onValueChange = { groupDescription = it },
            label = { Text("Group Description") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.createGroup(groupId, groupName, groupDescription) }) {
            Text("Create Group")
        }
    }
}