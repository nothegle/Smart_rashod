package com.example.proecticus.repository

import androidx.lifecycle.LiveData
import com.example.proecticus.db.Expense
import com.example.proecticus.db.ExpensesDao

class ExpensesRepository (private val expensesDao: ExpensesDao) {

    suspend fun getAllExpenses() = expensesDao.getAllExpenses()

    fun getExpensesByDay (date : String): LiveData<List<Expense>> = expensesDao.getExpensesByDay(date)

    val allExpensesInDB: LiveData<List<Expense>> = expensesDao.getAllExpenses()

    suspend fun insert(expense: Expense)  = expensesDao.insert(expense)

    suspend fun clearData() = expensesDao.clearData()
}
