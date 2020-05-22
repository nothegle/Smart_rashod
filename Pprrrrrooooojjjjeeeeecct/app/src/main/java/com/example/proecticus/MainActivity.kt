package com.example.proecticus

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    var sPref: SharedPreferences? = null
    val ALLMONEY = "ALLMONEY"
    val ALLEXPENCSES = "ALLEXPENCSES"

    companion object{
        const val ADD_REQUEST_CODE = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sdf = SimpleDateFormat("dd:MM:yy")
        val time = sdf.format(Date(System.currentTimeMillis()))
        Date.text = time
        loadData()
        //recyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = ExpensesListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun expensesOnClickListener(v: View?) {
        startActivityIntent(v)
    }


//Выполняется при закрытии активити с добалением расходов. Здесь данные должны отправляться в бд
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
        val newAllMon = allMon - newExp
        val newAllExp = allExp + newExp
        allMoney!!.text = newAllMon.toString()
        allExpenses!!.text = newAllExp.toString()
        var transactionID = generateTransactionID()

        when(data.getStringExtra("expenseItem")){

            "продукты" -> addProductsExpense(newExp)
            "транспорт" -> addTransportExpense(newExp)

        }

    }

    private fun addTransportExpense(newExp: Int){
        var transExp = transportExpenses.text.toString().toInt()
        var newTransExp = transExp + newExp
        transportExpenses.text = newTransExp.toString()
    }

    private fun addProductsExpense(newExp : Int){
        var prodExp = productsExpenses.text.toString().toInt()
        var newProdExp = prodExp + newExp
        productsExpenses.text = newProdExp.toString()
    }

    private fun generateTransactionID() : String = UUID.randomUUID().toString()

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

    private fun startActivityIntent(v: View?) {
        var intent = Intent(this, ActivityToAddExpense::class.java)
        if (v !is Button) return
        var expenseItemText = v.text
        intent.putExtra("expenseItemText", expenseItemText)
        startActivityForResult(intent, ADD_REQUEST_CODE)
    }
}