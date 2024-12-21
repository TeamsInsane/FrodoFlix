package com.frodo.frodoflix.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frodo.frodoflix.data.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNewUser(user: User)

    @Query("DELETE FROM User WHERE id =:id")
    suspend fun deleteExistingUser(id: Int)

    @Query("SELECT * FROM User")
    suspend fun getAllUsers(): Flow<List<User>>
}