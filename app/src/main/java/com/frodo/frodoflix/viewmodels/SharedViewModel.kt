package com.frodo.frodoflix.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.frodo.frodoflix.MainActivity
import com.frodo.frodoflix.data.Movie
import com.frodo.frodoflix.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    var selectedMovie: Movie? = null
    var navController: NavController? = null
    private val _isDarkTheme = mutableStateOf(false)
    val isDarkTheme: State<Boolean> = _isDarkTheme

    private val frodoDao = MainActivity.frodoDatabase.frodoDao()

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun newUser(username: String, email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
            val user = User(username = username, email = email, password = password)

            viewModelScope.launch(Dispatchers.IO) {
                frodoDao.insertNewUser(user)
            }
        }

    }

    fun deleteUser(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            frodoDao.deleteExistingUser(id)
        }
    }
}