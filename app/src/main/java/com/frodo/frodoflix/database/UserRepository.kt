package com.frodo.frodoflix.database

import com.frodo.frodoflix.data.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getAllUsersList(): Flow<List<User>>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(user: User)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(user: User)
}