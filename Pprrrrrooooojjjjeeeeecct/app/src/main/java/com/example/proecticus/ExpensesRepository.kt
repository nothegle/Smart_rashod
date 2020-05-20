package com.example.proecticus

import androidx.lifecycle.LiveData

class ExpensesRepository (private val expensesDao: ExpensesDao) {

    suspend fun getAllExpenses() = expensesDao.getAllExpenses()

    suspend fun getExpensesByDay (exp : Expenses): LiveData<List<Expenses>> {
        return expensesDao.getExpensesByDay(exp.date)
    }
}
