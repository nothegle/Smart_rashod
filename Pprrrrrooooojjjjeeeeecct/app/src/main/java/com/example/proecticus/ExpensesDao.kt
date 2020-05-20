package com.example.proecticus

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import java.time.OffsetDateTime

@Dao
interface ExpensesDao {

    @Query("SELECT * FROM EXPENSES_TABLE")
   fun getAllExpenses(): LiveData<List<Expenses>>

    @Query("SELECT * FROM expenses_table WHERE date = :date")
    fun getExpensesByDay(date : String): LiveData<List<Expenses>>
}