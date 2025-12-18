package com.frodo.frodoflix.screens.user

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.frodo.frodoflix.staticitems.BackToPreviousScreen
import com.frodo.frodoflix.staticitems.BottomMenuBar
import com.frodo.frodoflix.viewmodels.SharedViewModel


@Composable
fun DisplayUserPage(sharedViewModel: SharedViewModel) {
    val user = sharedViewModel.selectedUser
    val navController = sharedViewModel.navController ?: return


    if (user == null) {
        Log.d("user", "NULLLLL!")
        return
    }

    BackToPreviousScreen(navController)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {

        }
    }

}
