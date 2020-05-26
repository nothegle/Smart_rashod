package com.example.proecticus

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var sPref: SharedPreferences? = null
    val ALLMONEY = "ALLMONEY"
    val ALLEXPENCSES = "ALLEXPENCSES"

    private lateinit var mainActViewModel: MainActViewModel

    companion object {
        const val ADD_REQUEST_CODE = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = ExpensesListAdapter(applicationContext)
        recyclerview.adapter = adapter
        mainActViewModel = ViewModelProvider(this).get(MainActViewModel::class.java)
        mainActViewModel.allExpensesInDB.observe(this, Observer { exp ->
            exp?.let { adapter.setExpenses(it)
                expensesList = it }
        })

        mainActViewModel.viewModelScope.launch {

            val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(applicationContext)

            val sdf = SimpleDateFormat("dd:MM:yy")
            val time = sdf.format(Date(System.currentTimeMillis()))
            Date.text = time
            loadData()
            //берем из бд все расходы за текущий день по категориям
            if (expensesList.isEmpty().not()) {
                var listOfExpByDay = makeListOfExpensesByDay()
                var listOfProductsExp = expensesList.filter { x -> x.expCategory == "продукты" }// null
                var listOfTransportExp = expensesList.filter { x -> x.expCategory == "транспорт" }// null
                var sumOfProductsExp = listOfProductsExp?.sumBy { x -> x.sum }
                var sumOfTransportExp = listOfTransportExp?.sumBy { x -> x.sum }
                productsExpenses.text = sumOfProductsExp.toString()//textView
                transportExpenses.text = sumOfTransportExp.toString()//textView
            }
        }

    }
    var expensesList: List<Expense> = emptyList<Expense>() // PLAN B


    suspend fun makeListOfExpensesByDay(): LiveData<List<Expense>> {
        return mainActViewModel.allExpensesInDB
    }


    fun expensesOnClickListener(v: View?) {//слушает нажатия на кнопки с расходами
            startActivityIntent(v)
    }

    //Выполняется при закрытии активити с добалением расходов. Здесь данные должны отправляться в бд
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        val newExpense = data.getStringExtra("sumOfExpense")
        var newExp = 0
        if (newExpense != null) newExp = newExpense.toInt()
        val allMon = allMoney!!.text.toString().toInt()
        val allExp = allExpenses!!.text.toString().toInt()
        val newAllMon = allMon - newExp
        val newAllExp = allExp + newExp
        allMoney!!.text = newAllMon.toString()
        allExpenses!!.text = newAllExp.toString()
        var transactionID = generateTransactionID()

        var category: String? = data.getStringExtra("expenseItem")
        when {
            category == "продукты" -> addProductsExpense(newExp)
            category == "транспорт" -> addTransportExpense(newExp)
        }
//взаимодействие с бд
        val expenseForDataBase = Expense(transactionId = transactionID.toString(),
                expCategory = category.toString(),
                date = Date.text.toString(), sum = newExp)
        mainActViewModel.insert(expenseForDataBase)

    }

    private fun addTransportExpense(newExp: Int) {
        if (transportExpenses.text == "null")
            transportExpenses.text = newExp.toString()
        else {
            var transExp = transportExpenses.text.toString().toInt()
            var newTransExp = transExp + newExp
            transportExpenses.text = newTransExp.toString()
        }
    }

    private fun addProductsExpense(newExp: Int) {
        if (productsExpenses.text == "null")
            productsExpenses.text = newExp.toString()
        else {
            var prodExp = productsExpenses.text.toString().toInt()
            var newProdExp = prodExp + newExp
            productsExpenses.text = newProdExp.toString()
        }
    }

    private fun generateTransactionID(): String = UUID.randomUUID().toString()

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