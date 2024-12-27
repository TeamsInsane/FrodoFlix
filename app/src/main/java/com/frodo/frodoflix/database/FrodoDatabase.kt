package com.frodo.frodoflix.database;

import android.util.Log
import com.frodo.frodoflix.data.User
import com.google.firebase.Firebase
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

    suspend fun getWatchList(username: String): List<Int> {
        return try {
            val snapshot = database.getReference("users").child(username).child("watchlist").get().await()
            Log.d("firebase", snapshot.toString())
            snapshot.children.mapNotNull { it.getValue(Int::class.java) }
        } catch (e: Exception) {
            Log.e("Firebase", "Error getting watched list", e)
            emptyList()
        }
    }

    fun updateWatchedList(username: String, scope: CoroutineScope, watchlist: List<Int>) {
        scope.launch(Dispatchers.IO) {
            database.getReference("users").child(username).child("watchedlist").setValue(watchlist)
        }
    }

    fun updateWatchlist(username: String, scope: CoroutineScope, watchlist: List<Int>) {
        scope.launch(Dispatchers.IO) {
            database.getReference("users").child(username).child("watchlist").setValue(watchlist)
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

    fun newUser(username: String, email: String, password: String, salt: String, scope: CoroutineScope, genres: List<String> = emptyList()) {
        scope.launch(Dispatchers.IO){
            if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
                val user = User(username = username, email = email, password = password, salt = salt, genres = genres)
                Log.d("firebase", user.toString())
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
                    Log.d("Firebase", user.toString())
                    onResult(user)
                }
                .addOnFailureListener { exception ->
                    Log.e("Firebase", "Error getting data", exception)
                    onResult(null)
                }
        }
    }
}


