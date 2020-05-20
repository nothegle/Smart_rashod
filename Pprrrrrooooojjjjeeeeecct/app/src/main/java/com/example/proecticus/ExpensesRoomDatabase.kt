package com.example.proecticus

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Expenses::class), version = 1, exportSchema = false)
public abstract class ExpensesRoomDatabase : RoomDatabase() {

    abstract fun ExpensesDao(): ExpensesDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ExpensesRoomDatabase? = null

        fun getDatabase(context: Context): ExpensesRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ExpensesRoomDatabase::class.java,
                        "expenses_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}