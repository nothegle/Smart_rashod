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
import com.example.proecticus.ActivityToAddExpense.Companion.EXPENSE_ITEM_EXTRA
import com.example.proecticus.ActivityToAddExpense.Companion.SUM_OF_EXPENSE_EXTRA
import com.example.proecticus.adapter.ExpensesListAdapter
import com.example.proecticus.db.Expense
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

const val ALL_MONEY = "ALL_MONEY"
const val ALL_EXPENSES = "ALL_EXPENSES"

class MainActivity : AppCompatActivity() {

    private var sPref: SharedPreferences? = null

    private lateinit var mainActViewModel: MainActViewModel

    companion object {
        const val ADD_REQUEST_CODE = 1
        const val EXPENSE_ITEM_TEXT_EXTRA = "expenseItemText"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ExpensesListAdapter(applicationContext)
        recycler_view.adapter = adapter

        loadExpensesDataFromSharedPrefs()

        mainActViewModel = ViewModelProvider(this).get(MainActViewModel::class.java)

        mainActViewModel.allExpensesInDB.observe(this, Observer { exp ->
            exp?.let {
                adapter.setExpenses(it)
                updateExpensesTexts(it)
            }
        })
    }

    private fun makeListOfExpensesByDay(): LiveData<List<Expense>> {
        return mainActViewModel.allExpensesInDB
    }

    fun expensesOnClickListener(v: View?) {//слушает нажатия на кнопки с расходами
        if (v !is Button) return
        startActivityIntent(v)
    }

    //Выполняется при закрытии активити с добалением расходов. Здесь данные должны отправляться в бд
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data ?: return

        val newExpense = data.getStringExtra(SUM_OF_EXPENSE_EXTRA)
        var newExp = 0

        if (newExpense != null) newExp = newExpense.toInt()

        val allMon = all_money_tv.text.toString().toInt()
        val allExp = all_expenses_tv.text.toString().toInt()

        val newAllMon = allMon - newExp
        val newAllExp = allExp + newExp

        all_money_tv.text = newAllMon.toString()
        all_expenses_tv.text = newAllExp.toString()

        val category: String? = data.getStringExtra(EXPENSE_ITEM_EXTRA)
        when (category) {

            "продукты" -> addProductsExpense(newExp)
            "транспорт" -> addTransportExpense(newExp)
        }

        //взаимодействие с бд
        val expenseForDataBase = Expense(
            transactionId = generateTransactionID(),
            expCategory = category.toString(),
            date = date_tv.text.toString(), sum = newExp
        )
        mainActViewModel.insert(expenseForDataBase)
    }

    private fun addTransportExpense(newExp: Int) = when (transportExpenses.text) {

        "0" -> transportExpenses.text = newExp.toString()

        else -> {
            val transExp = transportExpenses.text.toString().toInt()
            val newTransExp = transExp + newExp
            transportExpenses.text = newTransExp.toString()
        }
    }

    private fun addProductsExpense(newExp: Int) = when (transportExpenses.text) {

        "0" -> transportExpenses.text = newExp.toString()

        else -> {
            val prodExp = productsExpenses.text.toString().toInt()
            val newProdExp = prodExp + newExp
            productsExpenses.text = newProdExp.toString()
        }
    }

    private fun generateTransactionID(): String = UUID.randomUUID().toString()

    private fun saveExpensesDataToSharedPrefs() {
        sPref = getPreferences(Context.MODE_PRIVATE)
        val ed = sPref!!.edit()
        ed.putString(ALL_MONEY, all_money_tv.text.toString())
        ed.putString(ALL_EXPENSES, all_expenses_tv.text.toString())
        ed.apply()
    }

    private fun loadExpensesDataFromSharedPrefs() {
        sPref = getPreferences(Context.MODE_PRIVATE)
        val savedAllMoney = sPref!!.getString(ALL_MONEY, "0")
        val savedAllExpenses = sPref!!.getString(ALL_EXPENSES, "0")
        all_money_tv.text = savedAllMoney
        all_expenses_tv.text = savedAllExpenses
    }

    private fun updateExpensesTexts(expenses: List<Expense>) {

        date_tv.text = SimpleDateFormat("dd.MM.yy").format(Date(System.currentTimeMillis()))

        //берем из бд все расходы за текущий день по категориям
        if (expenses.isEmpty().not()) {

            val listOfProductsExp = expenses.filter { x -> x.expCategory == "продукты" }
            val listOfTransportExp = expenses.filter { x -> x.expCategory == "транспорт" }

            val sumOfProductsExp = listOfProductsExp.sumBy { x -> x.sum }
            val sumOfTransportExp = listOfTransportExp.sumBy { x -> x.sum }

            productsExpenses.text = sumOfProductsExp.toString()//textView
            transportExpenses.text = sumOfTransportExp.toString()//textView
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveExpensesDataToSharedPrefs()
    }

    private fun startActivityIntent(btn: Button) {

        val intent = Intent(this, ActivityToAddExpense::class.java)
        val expenseItemText = btn.text
        intent.putExtra(EXPENSE_ITEM_TEXT_EXTRA, expenseItemText)
        startActivityForResult(intent, ADD_REQUEST_CODE)
    }
}