package com.example.proecticus

import android.provider.SyncStateContract.Helpers.insert
import androidx.lifecycle.LiveData

class ExpensesRepository (private val expensesDao: ExpensesDao) {

    suspend fun getAllExpenses() = expensesDao.getAllExpenses()

    suspend fun getExpensesByDay (date : String): LiveData<List<Expense>> {
        return expensesDao.getExpensesByDay(date)
    }

    val allExpensesInDB: LiveData<List<Expense>> = expensesDao.getAllExpenses()
    suspend fun insert(expense: Expense)  = expensesDao.insert(expense)
}