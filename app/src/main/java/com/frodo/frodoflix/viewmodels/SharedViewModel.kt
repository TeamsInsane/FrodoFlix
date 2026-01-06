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
import com.frodo.frodoflix.data.Group
import com.frodo.frodoflix.data.Message
import com.frodo.frodoflix.data.Movie
import com.frodo.frodoflix.data.Rating
import com.frodo.frodoflix.data.User
import com.frodo.frodoflix.data.UserCard
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
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import java.util.Locale

class SharedViewModel : ViewModel() {
    var selectedMovie: Movie? = null
    var selectedUser: UserCard? = null
    var movieSearchPrompt: String = ""
    var userSearchPrompt: String = ""

    var navController: NavController? = null
    private val databaseReference = FrodoDatabase()
    private var currentUser: User? = null
    lateinit var genresViewModel: GenresViewModel

    lateinit var sharedPreferences: SharedPreferences

    private val _isDarkTheme = mutableStateOf(false)
    val isDarkTheme: State<Boolean> = _isDarkTheme

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _watchlist = MutableStateFlow<List<Int>>(emptyList())
    val watchlist: StateFlow<List<Int>> = _watchlist.asStateFlow()

    private val _favList = MutableStateFlow<List<Int>>(emptyList())
    val favList: StateFlow<List<Int>> = _favList.asStateFlow()

    private val _watchedList = MutableStateFlow<List<Int>>(emptyList())
    val watchedList: StateFlow<List<Int>> = _watchedList.asStateFlow()

    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups.asStateFlow()

    private val _allGroups = MutableStateFlow<List<Group>>(emptyList())
    val allGroups: StateFlow<List<Group>> = _allGroups.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    fun sendMessage(groupId: String, content: String) {
        val username = currentUser?.username ?: return
        val message = Message(groupId, username, content, Date().time)
        databaseReference.sendMessage(message, viewModelScope)
    }

    fun listenForMessages(groupId: String) {
        _messages.value = emptyList()
        databaseReference.listenForMessages(groupId) { message ->
            _messages.update { currentMessages ->
                if (currentMessages.any { it.timestamp == message.timestamp && it.username == message.username }) {
                    currentMessages
                } else {
                    (currentMessages + message).sortedBy { it.timestamp }
                }
            }
        }
    }

    private fun loadDataFromDB() {
        val currentUser = this.currentUser
        if (currentUser == null) {
            Log.d("user", "ERROR null")
            return
        }

        viewModelScope.launch {
            val favList = databaseReference.getFavList(currentUser.username)
            currentUser.favlist = favList
            _favList.update { favList }

            val watchList = databaseReference.getWatchList(currentUser.username)
            currentUser.watchlist = watchList
            _watchlist.update { watchList }

            val watchedList = databaseReference.getWatchedList(currentUser.username)
            currentUser.watchedlist = watchedList
            _watchedList.update { watchedList }
        }

        loadUserGroups()
        loadThemeData()
    }

    fun createGroup(groupId: String, groupName: String, groupDescription: String, onResult: (Boolean) -> Unit) {
        val username = currentUser?.username ?: return
        _isLoading.value = true
        databaseReference.createGroup(groupId, groupName, groupDescription, username, viewModelScope) { success ->
            viewModelScope.launch(Dispatchers.Main) {
                loadUserGroups()
                _isLoading.value = false
                onResult(success)
            }
        }
    }

    fun joinGroup(groupId: String, onResult: (Boolean) -> Unit) {
        val username = currentUser?.username ?: return
        _isLoading.value = true
        databaseReference.joinGroup(groupId, username, "member", viewModelScope) { success ->
            viewModelScope.launch(Dispatchers.Main) {
                loadUserGroups()
                _isLoading.value = false
                onResult(success)
            }
        }
    }

    fun loadUserGroups() {
        val username = currentUser?.username ?: return
        databaseReference.getUserGroups(username, viewModelScope) { groups ->
            _groups.update { groups }
        }
    }

    fun searchGroups(query: String) {
        databaseReference.searchGroups(query = query, scope = viewModelScope, onResult = { groups ->
            _allGroups.value = groups
        })
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

    fun updateWatchedlist(movieID: Int) {
        _watchedList.update { currentList ->
            if (!currentList.contains(movieID)) {
                currentList + movieID
            } else {
                currentList - movieID
            }
        }

        databaseReference.updateWatchedlist(currentUser!!.username, viewModelScope, watchedList.value)
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

    fun newUser(username: String, email: String, password: String, salt: String, onResult: (Boolean) -> Unit) {
        fetchUser(username) { user ->
            if (user === null) {
                databaseReference.newUser(
                    username = username,
                    email = email,
                    password = password,
                    salt = salt,
                    viewModelScope
                )
                onResult(true)
                viewModelScope.launch(Dispatchers.Main) {
                    navController?.navigate("login_page")
                }
            } else {
                Log.e("Firebase","Username already taken!")
                onResult(false)
            }
        }
    }

    private fun fetchUser(username: String, onResult: (User?) -> Unit) {
        databaseReference.fetchUser(username = username, viewModelScope) { user ->
            onResult(user)
        }
    }

    fun checkLogin(username: String, password: String, onResult: (Boolean) -> Unit) {
        fetchUser(username) { user ->
            if (user != null) {
                if (verifyPassword(password, user.password, user.salt)) {
                    setCurrentUser(user)
                    saveLoginInfo(user.username, user.password)
                    loadDataFromDB()
                    onResult(true)

                    viewModelScope.launch(Dispatchers.Main) {
                        genresViewModel.loadGenresFromApi(user.genres)
                        genresViewModel.genresUiState.first { it.genresList.isNotEmpty() }

                        navController?.navigate("home_page")
                    }
                } else {
                    Log.e("Firebase", "Wrong username or password!")
                    onResult(false)
                }
            } else {
                Log.e("Firebase", "Wrong username or password!")
                onResult(false)
            }
        }
    }

    fun checkSavedLogin(callback: (Boolean) -> Unit) {
        val credentials = retrieveCredentials()
        val username = credentials.first
        val password = credentials.second

        if (username != null && password != null) {
            fetchUser(username) { user ->
                if (user != null) {
                    val result =  verifyHashedPassword(password, user.password)
                    if (result) {
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
        return hashedPassword == storedHash
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
            remove("isDarkTheme")
            remove("lastOnline")
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
                        databaseReference.changeRatingUsername(currentUser!!.username, username, viewModelScope)
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

    fun getLastOnlineTime(): String {
        val lastOnline = this.sharedPreferences.getLong("lastOnline", 0)

        return if (lastOnline > 0) {
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            formatter.format(lastOnline)
        } else {
            "Never"
        }
    }

    fun saveLastOnlineTime(time: Long) {
        try {
            with(this.sharedPreferences.edit()) {
                putLong("lastOnline", time)
                apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun searchUsers(userName: String, limit: Int, callback: (List<UserCard>) -> Unit) {
        val users = databaseReference.searchUsers(userName, limit)
        callback(users)
    }

    suspend fun doesUserFollow(
        user: UserCard
    ): Boolean {
        val myName = currentUser!!.username
        val targetName = user.username

        return databaseReference.isUserFollowed(myName, targetName)
    }

    fun toggleFollow(user: UserCard) {
        Log.d("toggle", user.username)

        if (currentUser == null) return

        Log.d("toggle", "mo kle")
        val myName = currentUser!!.username
        val targetName = user.username
        if (myName == targetName) return

        Log.d("toggle", "kle pa tud")
        viewModelScope.launch {
            val followed = databaseReference.isUserFollowed(myName, targetName)

            if (followed) {
                databaseReference.unfollowUser(myName, targetName)
            } else {
                databaseReference.followUser(myName, targetName)
            }
        }
    }
}