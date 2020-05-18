package com.example.proecticus

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class ActivityToAddRashod : AppCompatActivity() {
    var rashodToAdd: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_add_rashod)
        rashodToAdd = findViewById(R.id.rashodi)
    }

    @SuppressLint("SetTextI18n")
    fun Click_AddBtn(v: View?) {
        val newExpense = Intent()
        newExpense.putExtra("rashod", rashodToAdd!!.text.toString())
        setResult(Activity.RESULT_OK, newExpense)
        finish()
    }
}