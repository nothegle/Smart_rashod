package com.example.proecticus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.proecticus.MainActivity.Companion.EXPENSE_CATEGORY_TEXT_EXTRA
import kotlinx.android.synthetic.main.activity_to_add_expense.*

class ActivityToAddExpense : AppCompatActivity() {

    companion object {

        const val EXPENSE_AMOUNT_EXTRA = "expense amount"
        const val EXPENSE_CATEGORY_EXTRA = "expense category"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_add_expense)

        expense_category_tv.text = intent.getStringExtra(EXPENSE_CATEGORY_TEXT_EXTRA)
    }

    fun onAddBtnClick(v: View?) {

        val newExpense = Intent()

        newExpense.putExtra(EXPENSE_AMOUNT_EXTRA, add_expense_amount_et.text.toString())
        newExpense.putExtra(EXPENSE_CATEGORY_EXTRA, expense_category_tv.text.toString())

        setResult(Activity.RESULT_OK, newExpense)
        finish()
    }
}