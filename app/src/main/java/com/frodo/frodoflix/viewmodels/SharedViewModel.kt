package com.frodo.frodoflix.viewmodels

import android.content.SharedPreferences
import android.util.Log

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.frodo.frodoflix.data.Movie
import com.frodo.frodoflix.data.Rating
import com.frodo.frodoflix.data.User
import com.frodo.frodoflix.database.FrodoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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

    lateinit var sharedPreferences: SharedPreferences

    private val _isDarkTheme = mutableStateOf(false)
    val isDarkTheme: State<Boolean> = _isDarkTheme

    private val _watchlist = MutableStateFlow<List<Int>>(emptyList())
    val watchlist: StateFlow<List<Int>> = _watchlist.asStateFlow()

    private val _favList = MutableStateFlow<List<Int>>(emptyList())
    val favList: StateFlow<List<Int>> = _favList.asStateFlow()

    private fun loadDataFromDB() {
        val currentUser = this.currentUser
        if (currentUser == null) {
            Log.d("user", "ERORR null")
            return
        }

        viewModelScope.launch {
            val favList = databaseReference.getFavList(currentUser.username)
            currentUser.favlist = favList
            _favList.update { favList }

            val watchList = databaseReference.getWatchList(currentUser.username)
            currentUser.watchlist = watchList
            _watchlist.update { watchList }
        }

        loadThemeData()
    }

    private fun loadThemeData() {
        val darkTheme = this.sharedPreferences.getBoolean("isDarkTheme", false)
        if (darkTheme) {
            toggleTheme()
        }
    }

    suspend fun saveRating(rating: Int, comment: String) {
        val username = currentUser?.username ?: return

        val ratingList = getRatingList().toMutableList()
        ratingList.removeAll { it.username == username }
        ratingList.add(Rating(rating, comment, username))

        databaseReference.saveRating(ratingList, selectedMovie!!.id, viewModelScope)
    }

    suspend fun deleteRating() {
        databaseReference.deleteRating(currentUser!!.username, selectedMovie!!.id, viewModelScope)
    }

    suspend fun getRatingList(): List<Rating> {
        return databaseReference.getRatingList(selectedMovie!!.id)
    }

    fun initializeGenresViewModel(owner: ViewModelStoreOwner) {
        genresViewModel = ViewModelProvider(owner)[GenresViewModel::class.java]
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value

        try {
            with(this.sharedPreferences.edit()) {
                putBoolean("isDarkTheme", _isDarkTheme.value)
                apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateFavList(movieID: Int) {
        _favList.update { currentList ->
            if (!currentList.contains(movieID)) {
                currentList + movieID
            } else {
                currentList - movieID
            }
        }

        databaseReference.updateFavList(currentUser!!.username, viewModelScope, favList.value)
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

    private fun setCurrentUser(user: User) {
        this.currentUser = user
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
                viewModelScope.launch(Dispatchers.Main) {
                    navController?.navigate("login_page")
                }
            } else {
                Log.e("Firebase","Username already taken!")
                //TODO: Let the user know
            }
        }
    }

    private fun fetchUser(username: String, onResult: (User?) -> Unit) {
        databaseReference.fetchUser(username = username, viewModelScope) { user ->
            onResult(user)
        }
    }

    fun checkLogin(username: String, password: String) {
        fetchUser(username) { user ->
            if (user != null) {
                if (verifyPassword(password, user.password, user.salt)) {
                    setCurrentUser(user)
                    saveLoginInfo(user.username, user.password)
                    loadDataFromDB()

                    viewModelScope.launch(Dispatchers.Main) {
                        genresViewModel.loadGenresFromApi(user.genres)
                        genresViewModel.genresUiState.first { it.genresList.isNotEmpty() }

                        navController?.navigate("home_page")
                    }
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

    fun checkSavedLogin(callback: (Boolean) -> Unit) {
        val credentials = retrieveCredentials()
        val username = credentials.first
        val password = credentials.second

        if (username != null && password != null) {
            fetchUser(username) { user ->
                val result = user != null && verifyHashedPassword(password, user.password)
                if (result && user != null) {
                    setCurrentUser(user)
                    loadDataFromDB()

                    viewModelScope.launch {
                        genresViewModel.loadGenresFromApi(user.genres)
                        genresViewModel.genresUiState.first { it.genresList.isNotEmpty() }
                        callback(true)
                        navController?.navigate("home_page")
                    }
                } else {
                    callback(false)
                }
            }
        } else {
            callback(false)
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
        return verifyHashedPassword(inputHash, storedHash)
    }

    private fun verifyHashedPassword(hashedPassword: String, storedHash: String): Boolean {
        return hashedPassword == storedHash;
    }

    private fun retrieveCredentials(): Pair<String?, String?> {
        return try {
            val username = this.sharedPreferences.getString("username", null)
            val hashedPassword= this.sharedPreferences.getString("password", null)

            Pair(username, hashedPassword)
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(null, null)
        }
    }

    private fun saveLoginInfo(username: String, hashedPassword: String) {
        try {
            with(this.sharedPreferences.edit()) {
                putString("username", username)
                putString("password", hashedPassword)
                apply()
            }

            Log.d("savingData", "Saved $username $hashedPassword")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun signOut() {
        with(this.sharedPreferences.edit()) {
            remove("username")
            remove("password")
            apply()
        }

        viewModelScope.launch(Dispatchers.Main) {
            currentUser = null
            navController?.navigate("register_page")
        }
    }

    fun isUserSet(): Boolean {
        return currentUser != null
    }

    fun getUsername(): String {
        if (currentUser != null) {
            return currentUser!!.username
        }

        return ""
    }

    fun getEmail(): String {
        if (currentUser != null) {
            return currentUser!!.email
        }

        return ""
    }

    suspend fun getStarsRated(): Int{
        for (rating in getRatingList()) {
            if (rating.username == currentUser!!.username) {
                return rating.rating
            }
        }

        return -1
    }

    fun changeUsername(username: String, callback: (Boolean) -> Unit) {
        fetchUser(username) { user ->
            if (user != null) {
                callback(false)
            } else {
                this.databaseReference.changeUsername(currentUser!!.username, username, viewModelScope) { result ->
                    if (result) {
                        currentUser!!.username = username
                    }
                    callback(result)
                }
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String, callback: (Boolean) -> Unit) {
        fetchUser(currentUser!!.username) { user ->
            if (user != null) {
                if (verifyPassword(oldPassword, user.password, user.salt)) {
                    val hashedPassword = hashWithSalt(newPassword, user.salt)

                    this.databaseReference.changePassword(hashedPassword, currentUser!!.username, viewModelScope) { result ->
                        if (result) {
                            currentUser!!.password = hashedPassword
                        }
                        callback(result)
                    }
                } else {
                    callback(false)
                }
            } else {
                callback(false)
            }
        }
    }
}