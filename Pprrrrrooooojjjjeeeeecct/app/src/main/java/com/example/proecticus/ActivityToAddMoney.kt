package com.example.proecticus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_to_add_money.*
import androidx.appcompat.app.AppCompatActivity

class ActivityToAddMoney : AppCompatActivity() {
    companion object {
        const val TOTAL_AMOUNT_EXTRA = "total amount"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_add_money)
    }

    fun onAddMoneyClick(view: View?) {
        val newMoney = Intent()

        newMoney.putExtra(TOTAL_AMOUNT_EXTRA, add_total_amount_et.text.toString())

        setResult(Activity.RESULT_OK, newMoney)
        finish()
    }
}