package com.example.proecticus

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.chrono.HijrahChronology.INSTANCE


@Database(entities = arrayOf(Expense::class), version = 1, exportSchema = false)
public abstract class ExpensesRoomDatabase : RoomDatabase() {

    abstract fun ExpensesDao(): ExpensesDao

    private class ExpensesDatabaseCallback(
            private val scope: CoroutineScope
    ) : RoomDatabase.Callback(){
        override fun onOpen(db : SupportSQLiteDatabase){
            super.onOpen(db)
            INSTANCE?.let{database -> scope.launch { populateDatabase(database.ExpensesDao()) }}
        }
        suspend fun populateDatabase(expensesDao: ExpensesDao){
            var expense = Expense("0", "продукты", "26.05.20", 0)
            var expense1 = Expense("1", "транспорт", "26.05.20", 0)
            expensesDao.insert(expense)
            expensesDao.insert(expense1)
        }
    }
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ExpensesRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ExpensesRoomDatabase {
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

