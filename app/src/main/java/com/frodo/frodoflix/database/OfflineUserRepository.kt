package com.frodo.frodoflix.database

import com.frodo.frodoflix.data.User
import kotlinx.coroutines.flow.Flow
/*
class OfflineUserRepository(private val frodoDao: UserDao) : UserRepository {
    override suspend fun getAllUsersList(): Flow<List<User>> = frodoDao.getAllUsers();

    override suspend fun insertItem(user: User) = frodoDao.insertNewUser(user = user);

    override suspend fun deleteItem(user: User) = frodoDao.deleteExistingUser(user.id);
}*/