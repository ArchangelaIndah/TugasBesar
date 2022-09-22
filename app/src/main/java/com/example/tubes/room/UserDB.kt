package com.example.tubes.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room

@Database(
    entities = [User::class],
    version = 1 )
abstract class UserDB {
    abstract fun userDao() : UserDao
    companion object {
        @Volatile private var instance : UserDB? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                UserDB::class.java,
                "user12345.db"
            ).build()
    }

}