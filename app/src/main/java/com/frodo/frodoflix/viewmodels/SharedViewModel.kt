package com.frodo.frodoflix.viewmodels

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.frodo.frodoflix.data.Movie
import com.frodo.frodoflix.data.User
import com.frodo.frodoflix.database.FrodoDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

class SharedViewModel : ViewModel() {
    var selectedMovie: Movie? = null
    var navController: NavController? = null
    private val databaseReference = FrodoDatabase()
    private var currentUser: User? = null
    lateinit var genresViewModel: GenresViewModel

    private val _isDarkTheme = mutableStateOf(false)
    val isDarkTheme: State<Boolean> = _isDarkTheme

    private val _watchlist = MutableStateFlow<List<Int>>(emptyList())
    val watchlist: StateFlow<List<Int>> = _watchlist.asStateFlow()

    private val _watchedlist = MutableStateFlow<List<Int>>(emptyList())
    val watchedlist: StateFlow<List<Int>> = _watchedlist.asStateFlow()

    fun loadDataFromDB() {
        val currentUser = this.currentUser
        if (currentUser == null) {
            Log.d("user", "ERORR null")
            return
        }

        viewModelScope.launch {
            Log.d("firebase", currentUser.username)
            val watchedList = databaseReference.getWatchedList(currentUser.username)
            Log.d("watchlist", watchedList.toString())
            _watchedlist.update { watchedList }
            Log.d("Firebase", "Fetched watched list: $watchedList")
        }
    }

    fun initializeGenresViewModel(owner: ViewModelStoreOwner) {
        genresViewModel = ViewModelProvider(owner)[GenresViewModel::class.java]
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun updateWatchedlist(movieID: Int) {
        _watchedlist.update { currentList ->
            if (!currentList.contains(movieID)) {
                currentList + movieID
            } else {
                currentList - movieID
            }
        }

        databaseReference.updateWatchedList(currentUser!!.username, viewModelScope, watchedlist.value)
    }

    fun updateWatchlist(movieID: Int) {
        _watchlist.update { currentList ->
            if (!currentList.contains(movieID)) {
                currentList + movieID
            } else {
                currentList - movieID
            }
        }

        databaseReference.updateWatchlist(currentUser!!.username, viewModelScope, watchlist.value)
    }

    fun updateUserGenres(genreList: List<String>) {
        if (currentUser == null) {
            Log.d("Firebase", "User null")
            return
        }

        val tmpUser: User = currentUser as User
        currentUser = User(tmpUser.username, tmpUser.email, tmpUser.password, tmpUser.salt, genreList)

        databaseReference.updateGenreList(tmpUser.username, viewModelScope, genreList)
    }

    fun getUserGenres() : List<String>{
        return currentUser!!.genres
    }

    fun setCurrentUser(user: User) {
        this.currentUser = user;
    }

    fun newUser(username: String, email: String, password: String, salt: String) {
        fetchUser(username) { user ->
            if (user === null) {
                databaseReference.newUser(
                    username = username,
                    email = email,
                    password = password,
                    salt = salt,
                    viewModelScope
                )
                navController?.navigate("login_page")
            } else {
                Log.e("Firebase","Username already taken!")
                //TODO: Let the user know
            }
        }
    }

    fun fetchUser(username: String, onResult: (User?) -> Unit) {
        databaseReference.fetchUser(username = username, viewModelScope) { user ->
            onResult(user)
        }
    }

    fun checkLogin(username: String, password: String) {
        fetchUser(username) { user ->
            if (user != null) {
                if (verifyPassword(password, user.password, user.salt)) {
                    setCurrentUser(user)
                    loadDataFromDB()
                    navController?.navigate("home_page")
                } else {
                    Log.e("Firebase", "Wrong username or password!")
                    //TODO: Let the user know
                }
            } else {
                Log.e("Firebase", "Wrong username or password!")
                //TODO: Let the user know
            }
        }
    }

    fun hashPassword(password: String, saltLength: Int = 16): Pair<String, String> {
        val salt = ByteArray(saltLength)
        SecureRandom().nextBytes(salt)
        val saltBase64 = Base64.getEncoder().encodeToString(salt)

        val hashedPassword = hashWithSalt(password, saltBase64)

        return hashedPassword to saltBase64
    }

    private fun hashWithSalt(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val saltedPassword = password + salt
        val hash = digest.digest(saltedPassword.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(hash)
    }

    private fun verifyPassword(inputPassword: String, storedHash: String, storedSalt: String): Boolean {
        val inputHash = hashWithSalt(inputPassword, storedSalt)
        return inputHash == storedHash
    }
}