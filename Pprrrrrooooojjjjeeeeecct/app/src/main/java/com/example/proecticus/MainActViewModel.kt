package com.example.proecticus

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActViewModel(application: Application) : AndroidViewModel(application){
    private val repository: ExpensesRepository


    val allExpensesInDB: LiveData<List<Expense>>

    init {
        val expensesDao = ExpensesRoomDatabase.getDatabase(context = application,scope =  viewModelScope).ExpensesDao()
        repository = ExpensesRepository(expensesDao)
        allExpensesInDB =repository.allExpensesInDB
    }

    /**
     *  Launching a new coroutine
     */
    fun insert(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(expense)
    }
}