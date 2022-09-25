package com.example.tubes.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Reservasi::class],
    version = 1 )


abstract class ReservasiDB : RoomDatabase() {
    abstract fun reservasiDao() : ReservasiDao
    companion object {
        @Volatile private var instance : ReservasiDB? = null
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
                ReservasiDB::class.java,
                "reservasi12345.db"
            ).build()
    }
}