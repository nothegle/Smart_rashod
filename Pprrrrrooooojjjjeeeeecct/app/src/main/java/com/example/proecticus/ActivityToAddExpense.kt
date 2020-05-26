package com.example.proecticus

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_to_add_expense.*

class ActivityToAddExpense : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_add_expense)
        expenseItem.text = getIntent().getStringExtra("expenseItemText")
    }

    @SuppressLint("SetTextI18n")
    fun Click_AddBtn(v: View?) {
        val newExpense = Intent()
        newExpense.putExtra("sumOfExpense", expenseInt!!.text.toString())
        newExpense.putExtra("expenseItem", expenseItem.text)
        setResult(Activity.RESULT_OK, newExpense)
        finish()
    }
}