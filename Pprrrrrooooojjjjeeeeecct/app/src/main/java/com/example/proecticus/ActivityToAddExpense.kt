package com.example.proecticus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.proecticus.MainActivity.Companion.EXPENSE_ITEM_TEXT_EXTRA
import kotlinx.android.synthetic.main.activity_to_add_expense.*

class ActivityToAddExpense : AppCompatActivity() {

    companion object {

        const val SUM_OF_EXPENSE_EXTRA = "sumOfExpense"
        const val EXPENSE_ITEM_EXTRA = "expenseItem"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_add_expense)
        expense_item_tv.text = intent.getStringExtra(EXPENSE_ITEM_TEXT_EXTRA)
    }

    fun onAddBtnClick(v: View?) {
        val newExpense = Intent()
        newExpense.putExtra(SUM_OF_EXPENSE_EXTRA, add_expense_amount_et.text.toString())
        newExpense.putExtra(EXPENSE_ITEM_EXTRA, expense_item_tv.text.toString())
        setResult(Activity.RESULT_OK, newExpense)
        finish()
    }
}