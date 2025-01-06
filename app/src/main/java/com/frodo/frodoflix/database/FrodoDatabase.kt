package com.frodo.frodoflix.database

import android.util.Log
import com.frodo.frodoflix.data.Rating
import com.frodo.frodoflix.data.User
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FrodoDatabase {
    private val dotenv = dotenv {
        directory = "/assets"
        filename = "env"
    }

    private val DATABASE_REFERENCE: String = dotenv["DATABASE_REFERENCE"]
    private val database = Firebase.database(DATABASE_REFERENCE)

    suspend fun getFavList(username: String): List<Int> {
        return try {
            val snapshot = database.getReference("users").child(username).child("favlist").get().await()
            Log.d("firebase", snapshot.toString())
            snapshot.children.mapNotNull { it.getValue(Int::class.java) }
        } catch (e: Exception) {
            Log.e("Firebase", "Error getting fav list", e)
            emptyList()
        }
    }

    suspend fun getWatchedList(username: String): List<Int> {
        return try {
            val snapshot = database.getReference("users").child(username).child("watchedlist").get().await()
            Log.d("firebase", snapshot.toString())
            snapshot.children.mapNotNull { it.getValue(Int::class.java) }
        } catch (e: Exception) {
            Log.e("Firebase", "Error getting watched list", e)
            emptyList()
        }
    }


    suspend fun getRatingList(movieID: Int): List<Rating> {
        return try {
            val snapshot = database.getReference("movies").child(movieID.toString()).get().await()
            Log.d("firebase", snapshot.toString())
            snapshot.children.mapNotNull { it.getValue(Rating::class.java) }
        } catch (e: Exception) {
            Log.e("Firebase", "Error getting rating list", e)
            emptyList()
        }
    }

    suspend fun deleteRating(username: String, movieID: Int, scope: CoroutineScope) {
        val snapshot = database.getReference("movies").child(movieID.toString()).get().await()
        val ratingList = snapshot.children.mapNotNull { it.getValue(Rating::class.java) }.toMutableList()

        ratingList.removeIf { it.username == username }

        saveRating(ratingList, movieID, scope)
    }

    suspend fun getWatchList(username: String): List<Int> {
        return try {
            val snapshot = database.getReference("users").child(username).child("watchlist").get().await()
            Log.d("firebase", snapshot.toString())
            snapshot.children.mapNotNull { it.getValue(Int::class.java) }
        } catch (e: Exception) {
            Log.e("Firebase", "Error getting watch list", e)
            emptyList()
        }
    }

    fun saveRating(ratingList: MutableList<Rating>, movieID: Int, scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            database.getReference("movies").child(movieID.toString()).setValue(ratingList)
        }
    }

    fun updateFavList(username: String, scope: CoroutineScope, favList: List<Int>) {
        scope.launch(Dispatchers.IO) {
            database.getReference("users").child(username).child("favlist").setValue(favList)
        }
    }

    fun updateWatchlist(username: String, scope: CoroutineScope, watchlist: List<Int>) {
        scope.launch(Dispatchers.IO) {
            database.getReference("users").child(username).child("watchlist").setValue(watchlist)
        }
    }

    fun updateWatchedlist(username: String, scope: CoroutineScope, watchlist: List<Int>) {
        scope.launch(Dispatchers.IO) {
            database.getReference("users").child(username).child("watchedlist").setValue(watchlist)
        }
    }

    fun updateGenreList(username: String, scope: CoroutineScope, genres: List<String>) {
        scope.launch(Dispatchers.IO) {
            database.getReference("users").child(username).child("genres").setValue(genres)
                .addOnFailureListener { exception ->
                    Log.e("Firebase", "Ni ratal shrant v db", exception)
                    //TODO: Let the user know
                }
        }
    }

    fun newUser(username: String, email: String, password: String, salt: String, scope: CoroutineScope, genres: List<String> = emptyList(), favList: List<Int> = emptyList(), watchlist: List<Int> = emptyList(), watchedlist: List<Int> = emptyList()) {
        scope.launch(Dispatchers.IO){
            if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
                val user = User(username = username, email = email, password = password, salt = salt, genres = genres, favlist = favList, watchlist = watchlist, watchedlist = watchedlist)
                database.getReference("users").child(username).setValue(user)
                    .addOnSuccessListener {
                        Log.d("Firebase", "Ratal shrant v db")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Firebase", "Ni ratal shrant v db", exception)
                        //TODO: Let the user know
                    }
            } else {
                Log.d("Firebase", "if ne dela")
            }
        }
    }

    fun fetchUser(username: String, scope: CoroutineScope, onResult: (User?) -> Unit) {
        scope.launch(Dispatchers.IO) {
            database.getReference("users").child(username).get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(User::class.java)
                    Log.d("user", user.toString())
                    onResult(user)
                }
                .addOnFailureListener { exception ->
                    Log.e("Firebase", "Error getting data", exception)
                    onResult(null)
                }
        }
    }

    fun changeUsername(oldUsername: String, newUsername: String, scope: CoroutineScope, onResult: (Boolean) -> Unit) {
        scope.launch(Dispatchers.IO){
            database.getReference("users").child(oldUsername).get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(User::class.java)

                    if (user != null) {
                        database.getReference("users").child(oldUsername).removeValue()
                        newUser(newUsername, user.email, user.password, user.salt, scope, user.genres, user.favlist, user.watchlist, user.watchedlist)
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                }

                .addOnFailureListener { exception ->
                    Log.e("Firebase", "Error getting data", exception)
                    onResult(false)
                }
        }
    }

    fun changePassword(newPassword: String, username: String, scope: CoroutineScope, onResult: (Boolean) -> Unit) {
        scope.launch(Dispatchers.IO) {
            database.getReference("users").child(username).get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(User::class.java)

                    if (user != null) {
                        database.getReference("users").child(username).removeValue()
                        newUser(user.username, user.email, newPassword, user.salt, scope, user.genres, user.favlist, user.watchlist, user.watchedlist)
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                }

                .addOnFailureListener { exception ->
                    Log.e("Firebase", "Error getting data", exception)
                    onResult(false)
                }
        }
    }

    fun changeRatingUsername(oldUsername: String, newUsername: String, scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            val moviesRef = database.getReference("movies")

            moviesRef.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (movieSnapshot in snapshot.children) {
                        for (reviewSnapshot in movieSnapshot.children) {
                            val username = reviewSnapshot.child("username").getValue(String::class.java)

                            Log.d("review", username!!)

                            if (username == oldUsername) {
                                reviewSnapshot.ref.child("username").setValue(newUsername)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }
}


