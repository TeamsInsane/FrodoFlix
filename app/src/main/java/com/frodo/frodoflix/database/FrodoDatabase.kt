package com.frodo.frodoflix.database;

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.frodo.frodoflix.data.User

@Database(entities = [User::class], version = 1)
@TypeConverters(Converters::class)
abstract class FrodoDatabase : RoomDatabase() {
    abstract fun frodoDao(): FrodoDao

    companion object {
        const val NAME = "Frodo"
    }
}
