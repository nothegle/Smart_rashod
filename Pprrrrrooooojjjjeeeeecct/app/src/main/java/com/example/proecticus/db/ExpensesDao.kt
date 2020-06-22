package com.example.proecticus.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExpensesDao {

    @Query("SELECT * FROM EXPENSES_TABLE")
   fun getAllExpenses(): LiveData<List<Expense>>

    @Query("SELECT * FROM expenses_table WHERE date = :date")
    fun getExpensesByDay(date : String): LiveData<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expense: Expense)

    @Query("DELETE FROM EXPENSES_TABLE")
    suspend fun clearData()
}