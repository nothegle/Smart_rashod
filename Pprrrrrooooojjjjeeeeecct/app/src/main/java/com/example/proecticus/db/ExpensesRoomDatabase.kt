package com.example.proecticus.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Expense::class], version = 1, exportSchema = false)
abstract class ExpensesRoomDatabase : RoomDatabase() {

    abstract fun expensesDao(): ExpensesDao

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

    private class ExpensesDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database -> scope.launch { populateDatabase(database.expensesDao()) } }
        }

        suspend fun populateDatabase(expensesDao: ExpensesDao) {
            val expense = Expense("0", "продукты", "26.05.20", 0)
            val expense1 = Expense("1", "транспорт", "26.05.20", 0)
            expensesDao.insert(expense)
            expensesDao.insert(expense1)
        }
    }
}

