package com.frodo.frodoflix.screens.user

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.viewmodels.SharedViewModel


@Composable
fun DisplayUserPage(sharedViewModel: SharedViewModel) {
    //val user = sharedViewModel.selectedUser
    val navController = sharedViewModel.navController ?: return

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {

        }
    }

    BottomMenuBar(navController)
}
