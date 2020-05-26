package com.example.proecticus.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExpensesDao {

    @Query("SELECT * FROM EXPENSES_TABLE")
   fun getAllExpenses(): LiveData<List<Expense>>

    @Query("SELECT * FROM expenses_table WHERE date = :date")
    fun getExpensesByDay(date : String): LiveData<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expense: Expense)

}