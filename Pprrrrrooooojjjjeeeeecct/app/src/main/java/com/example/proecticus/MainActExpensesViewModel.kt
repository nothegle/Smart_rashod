package com.example.proecticus

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.activity_main.*

class MainActExpensesViewModel(application: Application) : AndroidViewModel(application){
    private val repository: ExpensesRepository
    var allMoney: TextView? = null
    var allExpenses: TextView? = null

    init {
        val expensesDao = ExpensesRoomDatabase.getDatabase(application).ExpensesDao()
        repository = ExpensesRepository(expensesDao)
    }

    /**
     *  Launching a new coroutine
     */
    fun addProductsExpense(){

    }
}