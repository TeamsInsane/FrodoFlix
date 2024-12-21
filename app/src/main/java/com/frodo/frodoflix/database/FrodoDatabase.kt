package com.frodo.frodoflix.database;

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.frodo.frodoflix.data.User

@Database(entities = [User::class], version = 1)
@TypeConverters(Converters::class)
abstract class FrodoDatabase : RoomDatabase() {
    abstract fun frodoDao(): UserDao

    companion object {
        @Volatile
        private var Instance: FrodoDatabase? = null

        fun getDatabase(context: Context): FrodoDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FrodoDatabase::class.java, "FrodoDatabase")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
