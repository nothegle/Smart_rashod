package com.example.proecticus

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExpensesRepository

    val allExpensesInDB: LiveData<List<Expense>>

    init {
        val expensesDao = ExpensesRoomDatabase.getDatabase(context = application, scope = viewModelScope).expensesDao()
        repository = ExpensesRepository(expensesDao)
        allExpensesInDB = repository.allExpensesInDB
    }

    /**
     *  Launching a new coroutine
     */
    fun insert(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(expense)
    }

    suspend fun getExpensesByDay(date: String): LiveData<List<Expense>> {
        return repository.getExpensesByDay(date)
    }

    suspend fun getAllExpenses(): LiveData<List<Expense>> {
        return repository.getAllExpenses()
    }
}