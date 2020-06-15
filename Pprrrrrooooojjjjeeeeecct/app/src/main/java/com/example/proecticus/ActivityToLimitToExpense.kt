package com.example.proecticus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_to_add_limit.*

class ActivityToLimitToExpense : AppCompatActivity() {
    companion object{
        const val ADD_LIMIT_EXTRA = "ADD_LIMIT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_add_limit)
    }

    fun onAddLimitClick(view: View?) {
        val newLimit = Intent()
        newLimit.putExtra(ADD_LIMIT_EXTRA, new_limit_et.text.toString())

        setResult(Activity.RESULT_OK, newLimit)
        finish()
    }


}