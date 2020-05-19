package com.example.proecticus

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity() {
    private var products: Button? = null
    var allMoney: TextView? = null
    var allExpenses: TextView? = null
    var productsExpenses: TextView? = null
    var sPref: SharedPreferences? = null
    val ALLMONEY = "ALLMONEY"
    val ALLEXPENCSES = "ALLEXPENCSES"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sdf = SimpleDateFormat("dd:MM:yy")
        val time = sdf.format(java.util.Date(System.currentTimeMillis()))
        Date.text = time
        allMoney = findViewById(R.id.allMoney)
        allExpenses = findViewById(R.id.allExpenses)
        products = findViewById(R.id.products)
        productsExpenses = findViewById(R.id.productsExpenses)
        loadData()
    }

    fun сlick_Products(v: View?) {
        val intent = Intent(this, ActivityToAddRashod::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        val newExpense = data.getStringExtra("rashod")
        var newExp = 0
        if (newExpense != null) newExp = newExpense.toInt()
        val allMon = allMoney!!.text.toString().toInt()
        val allExp = allExpenses!!.text.toString().toInt()
        val prodExp = productsExpenses!!.text.toString().toInt()
        val newProdExp = prodExp + newExp
        val newAllMon = allMon - newExp
        val newAllExp = allExp + newExp
        productsExpenses!!.text = Integer.toString(newProdExp)
        allMoney!!.text = Integer.toString(newAllMon)
        allExpenses!!.text = Integer.toString(newAllExp)
    }

    private fun saveData() {
        sPref = getPreferences(Context.MODE_PRIVATE)
        val ed = sPref!!.edit()
        ed.putString(ALLMONEY, allMoney!!.text.toString())
        ed.putString(ALLEXPENCSES, allExpenses!!.text.toString())
        ed.commit()
    }

    private fun loadData() {
        sPref = getPreferences(Context.MODE_PRIVATE)
        val savedAllMoney = sPref!!.getString(ALLMONEY, "0")
        val savedAllExpenses = sPref!!.getString(ALLEXPENCSES, "0")
        allMoney!!.text = savedAllMoney
        allExpenses!!.text = savedAllExpenses
    }

    override fun onDestroy() {
        super.onDestroy()
        saveData()
    }
}