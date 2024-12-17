package com.frodo.frodoflix.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.frodo.frodoflix.data.User


@Dao
interface FrodoDao {
    @Insert
    fun insertNewUser(user: User)

    @Query("DELETE FROM User WHERE id =:id")
    fun deleteExistingUser(id: Int)

    @Query("SELECT * FROM User")
    fun getAllUsers(): LiveData<List<User>>
}