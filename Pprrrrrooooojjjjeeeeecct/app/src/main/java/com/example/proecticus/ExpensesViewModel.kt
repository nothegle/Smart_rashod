package com.example.proecticus

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ExpensesViewModel(application: Application) : AndroidViewModel(application){
    private val repository: ExpensesRepository

    init {
        val expensesDao = ExpensesRoomDatabase.getDatabase(application).ExpensesDao()
        repository = ExpensesRepository(expensesDao)
    }

    /**
     *  Launching a new coroutine
     */

}