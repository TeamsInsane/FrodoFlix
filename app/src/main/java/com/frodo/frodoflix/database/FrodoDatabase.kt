package com.frodo.frodoflix.database

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.setValue
import com.frodo.frodoflix.data.Group
import com.frodo.frodoflix.data.GroupMember
import com.frodo.frodoflix.data.Message
import com.frodo.frodoflix.data.Rating
import com.frodo.frodoflix.data.User
import com.frodo.frodoflix.data.UserCard
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.messaging.FirebaseMessaging
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

    fun sendMessage(message: Message, scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            database.getReference("messages").child(message.groupId!!).push().setValue(message).await()
        }
    }

    fun listenForMessages(groupId: String, onMessage: (Message) -> Unit) {
        val messagesRef = database.getReference("messages").child(groupId)
        messagesRef.orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.mapNotNull { it.getValue(Message::class.java) }.forEach(onMessage)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error getting messages", error.toException())
            }
        })
    }

    fun createGroup(groupId: String, groupName: String, groupDescription: String, createdBy: String, scope: CoroutineScope, onResult: (Boolean) -> Unit) {
        scope.launch(Dispatchers.IO) {
            try {
                val groupRef = database.getReference("groups").child(groupId)
                val snapshot = groupRef.get().await()
                if (snapshot.exists()) {
                    onResult(false)
                } else {
                    val group = Group(groupId, groupName, groupDescription, createdBy)
                    groupRef.setValue(group).await()
                    joinGroup(groupId, createdBy, "admin", scope) { success ->
                        onResult(success)
                    }
                }
            } catch (e: Exception) {
                Log.e("Firebase", "Error creating group", e)
                onResult(false)
            }
        }
    }

    fun joinGroup(groupId: String, username: String, role: String, scope: CoroutineScope, onResult: (Boolean) -> Unit) {
        scope.launch(Dispatchers.IO) {
            try {
                val member = GroupMember(role)
                database.getReference("groups").child(groupId).child("members").child(username).setValue(member).await()
                database.getReference("users").child(username).child("groups").child(groupId).setValue(member).await()
                onResult(true)
            } catch (e: Exception) {
                Log.e("Firebase", "Error joining group", e)
                onResult(false)
            }
        }
    }

    fun getUserGroups(username: String, scope: CoroutineScope, onResult: (List<Group>) -> Unit) {
        scope.launch(Dispatchers.IO) {
            val groupIdsSnapshot = database.getReference("users").child(username).child("groups").get().await()
            val groupIds = groupIdsSnapshot.children.map { it.key!! }
            val groups = mutableListOf<Group>()
            for (groupId in groupIds) {
                val groupSnapshot = database.getReference("groups").child(groupId).get().await()
                val group = groupSnapshot.getValue(Group::class.java)
                if (group != null) {
                    groups.add(group)
                }
            }
            onResult(groups)
        }
    }

    fun searchGroups(query: String, scope: CoroutineScope, onResult: (List<Group>) -> Unit) {
        scope.launch(Dispatchers.IO) {
            val groupsRef = database.getReference("groups")
            groupsRef.orderByChild("groupName")
                .startAt(query)
                .endAt(query + "")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val groups = mutableListOf<Group>()
                        for (groupSnapshot in snapshot.children) {
                            val group = groupSnapshot.getValue(Group::class.java)
                            if (group != null) {
                                groups.add(group)
                            }
                        }
                        onResult(groups)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Firebase", "Error searching groups", error.toException())
                        onResult(emptyList())
                    }
                })
        }
    }

    suspend fun getFavList(username: String): List<Int> {
        return try {
            val snapshot = database.getReference("users").child(username).child("favlist").get().await()
            snapshot.children.mapNotNull { it.getValue(Int::class.java) }
        } catch (e: Exception) {
            Log.e("Firebase", "Error getting fav list", e)
            emptyList()
        }
    }

    suspend fun getWatchedList(username: String): List<Int> {
        return try {
            val snapshot = database.getReference("users").child(username).child("watchedlist").get().await()
            snapshot.children.mapNotNull { it.getValue(Int::class.java) }
        } catch (e: Exception) {
            Log.e("Firebase", "Error getting watched list", e)
            emptyList()
        }
    }


    suspend fun getRatingList(movieID: Int): List<Rating> {
        return try {
            val snapshot = database.getReference("movies").child(movieID.toString()).get().await()
            snapshot.children.mapNotNull { it.getValue(Rating::class.java) }
        } catch (e: Exception) {
            Log.e("Firebase", "Error getting rating list", e)
            emptyList()
        }
    }

    suspend fun deleteRating(username: String, movieID: Int, scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            database.getReference("user_ratings").child(username).child(movieID.toString()).removeValue()

            val snapshot = database.getReference("movies").child(movieID.toString()).get().await()
            val ratingList = snapshot.children.mapNotNull { it.getValue(Rating::class.java) }.toMutableList()

            ratingList.removeIf { it.username == username }

            database.getReference("movies").child(movieID.toString()).setValue(ratingList)
        }
    }

    suspend fun getWatchList(username: String): List<Int> {
        return try {
            val snapshot = database.getReference("users").child(username).child("watchlist").get().await()
            snapshot.children.mapNotNull { it.getValue(Int::class.java) }
        } catch (e: Exception) {
            Log.e("Firebase", "Error getting watch list", e)
            emptyList()
        }
    }

    fun saveRating(rating: Rating, movieID: Int, username: String, scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            database.getReference("user_ratings").child(username).child(movieID.toString()).setValue(rating)

            val snapshot = database.getReference("movies").child(movieID.toString()).get().await()
            val ratingList = snapshot.children.mapNotNull { it.getValue(Rating::class.java) }.toMutableList()
            ratingList.removeIf { it.username == username }
            ratingList.add(rating)

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
                    }
            }
        }
    }

    fun fetchUser(username: String, scope: CoroutineScope, onResult: (User?) -> Unit) {
        scope.launch(Dispatchers.IO) {
            database.getReference("users").child(username).get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(User::class.java)
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

                            if (username == oldUsername) {
                                reviewSnapshot.ref.child("username").setValue(newUsername)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

            val oldUserRatingsRef = database.getReference("user_ratings").child(oldUsername)
            oldUserRatingsRef.get().addOnSuccessListener { userRatingsSnapshot ->
                if (userRatingsSnapshot.exists()) {
                    val ratingsData = userRatingsSnapshot.value as? Map<*, *>
                    if (ratingsData != null) {
                        val newRatingsData = ratingsData.mapValues {
                            val ratingMap = it.value as Map<*, *>
                            ratingMap.plus("username" to newUsername)
                        }

                        val newUserRatingsRef = database.getReference("user_ratings").child(newUsername)
                        newUserRatingsRef.setValue(newRatingsData).addOnSuccessListener {
                            oldUserRatingsRef.removeValue()
                        }
                    }
                }
            }
        }
    }

    fun saveProfileImageUrl(username: String, url: String) {
        database
            .getReference("users")
            .child(username)
            .child("profileImageUrl")
            .setValue(url)
    }

    suspend fun getProfileImageUrl(username: String): String? {
        return database
            .getReference("users")
            .child(username)
            .child("profileImageUrl")
            .get()
            .await()
            .getValue(String::class.java)
    }


    suspend fun searchUsers(
        query: String,
        limit: Int = 20
    ): List<UserCard> {
        return try {
            val snapshot = database
                .getReference("users")
                .orderByChild("username")
                .startAt(query)
                .endAt(query + "")
                .limitToFirst(limit)
                .get()
                .await()

            snapshot.children.mapNotNull {
                it.getValue(UserCard::class.java)
            }
        } catch (e: Exception) {
            Log.e("Firebase", "Error searching users", e)
            emptyList()
        }
    }

    suspend fun isUserFollowed(
        myName: String,
        targetName: String
    ): Boolean {
        return try {
            val snapshot = database
                .getReference("followers")
                .child(targetName)
                .child(myName)
                .get()
                .await()

            snapshot.exists()
        } catch (e: Exception) {
            false
        }
    }


    fun followUser(myName: String, targetName: String) {
        val updates = hashMapOf<String, Any>(
            "/followers/$targetName/$myName" to true,
            "following/$myName/$targetName" to true,
            "/users/$targetName/followersCount" to ServerValue.increment(1),
            "/users/$myName/followingCount" to ServerValue.increment(1)
        )

        database.getReference().updateChildren(updates)
    }

    fun unfollowUser(myName: String, targetName: String) {
        val updates = hashMapOf<String, Any?>(
            "/followers/$targetName/$myName" to null,
            "/following/$myName/$targetName" to null,
            "/users/$targetName/followersCount" to ServerValue.increment(-1),
            "/users/$myName/followingCount" to ServerValue.increment(-1),
        )

        database.getReference().updateChildren(updates)
    }

    suspend fun getFollowing(username: String): List<String> {
        return try {
            val followingSnapshot = database.getReference("following").child(username).get().await()
            followingSnapshot.children.mapNotNull { it.key }
        } catch (e: Exception) {
            Log.e("Firebase", "Error getting following list", e)
            emptyList()
        }
    }

    suspend fun getUserRatings(username: String): List<Rating> {
        return try {
            val userRatingsSnapshot = database.getReference("user_ratings").child(username).get().await()
            userRatingsSnapshot.children.mapNotNull { it.getValue(Rating::class.java) }
        } catch (e: Exception) {
            Log.e("Firebase", "Error getting user ratings", e)
            emptyList()
        }
    }

    fun subscribeToGroupTopic(groupId: String) {
        FirebaseMessaging.getInstance()
            .subscribeToTopic("group_$groupId")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to topic group_$groupId")
                } else {
                    Log.e("FCM", "Failed to subscribe to topic group_$groupId")
                }
            }
    }

    fun unsubscribeFromGroupTopic(groupId: String) {
        FirebaseMessaging.getInstance()
            .unsubscribeFromTopic("group_$groupId")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Unsubscribed to topic group_$groupId")
                } else {
                    Log.e("FCM", "Failed to unsubscribe to topic group_$groupId")
                }
            }
    }
}