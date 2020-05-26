package com.example.proecticus

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.proecticus.MainActivity.Companion.EXPENSE_ITEM_TEXT_EXTRA
import kotlinx.android.synthetic.main.activity_to_add_expense.*

class ActivityToAddExpense : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_add_expense)
        expenseItem.text = intent.getStringExtra(EXPENSE_ITEM_TEXT_EXTRA)
    }

    @SuppressLint("SetTextI18n")
    fun onAddBtnClick(v: View?) {
        val newExpense = Intent()
        newExpense.putExtra("sumOfExpense", expenseInt.text.toString())
        newExpense.putExtra("expenseItem", expenseItem.text.toString())
        setResult(Activity.RESULT_OK, newExpense)
        finish()
    }
}