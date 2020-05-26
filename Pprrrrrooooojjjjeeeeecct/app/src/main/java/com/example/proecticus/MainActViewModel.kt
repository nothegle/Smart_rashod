package com.example.proecticus

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.proecticus.db.Expense
import com.example.proecticus.db.ExpensesRoomDatabase
import com.example.proecticus.repository.ExpensesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val EXPENSES_SHARED_PREFS = "expenses shared prefs"

class MainActViewModel(private val app: Application) : AndroidViewModel(app) {

    private val repository: ExpensesRepository

    val allExpensesInDB: LiveData<List<Expense>>

    init {
        val expensesDao = ExpensesRoomDatabase.getDatabase(context = app, scope = viewModelScope).expensesDao()
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

    fun loadExpensesDataFromSharedPrefs(): ExpensesTextHolder {

        val sPref = app.getSharedPreferences(EXPENSES_SHARED_PREFS, Context.MODE_PRIVATE)
        val savedAllMoney = sPref.getString(ALL_MONEY, "0") ?: "0"
        val savedAllExpenses = sPref.getString(ALL_EXPENSES, "0") ?: "0"

        return ExpensesTextHolder(allMoneyText = savedAllMoney, allExpensesText = savedAllExpenses)
    }

    fun saveExpensesDataToSharedPrefs(expensesTexts: ExpensesTextHolder) {

        val sPref = app.getSharedPreferences(EXPENSES_SHARED_PREFS, Context.MODE_PRIVATE)
        val ed = sPref!!.edit()
        ed.putString(ALL_EXPENSES, expensesTexts.allExpensesText)
        ed.putString(ALL_MONEY, expensesTexts.allMoneyText)
        ed.apply()
    }
}