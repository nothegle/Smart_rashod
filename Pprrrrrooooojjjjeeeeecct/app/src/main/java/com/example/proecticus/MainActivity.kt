package com.example.proecticus

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proecticus.ActivityToAddExpense.Companion.EXPENSE_AMOUNT_EXTRA
import com.example.proecticus.ActivityToAddExpense.Companion.EXPENSE_CATEGORY_EXTRA
import com.example.proecticus.adapter.ExpensesListAdapter
import com.example.proecticus.data.ExpenseCategory.PRODUCTS
import com.example.proecticus.data.ExpenseCategory.TRANSPORT
import com.example.proecticus.data.ExpensesTextHolder
import com.example.proecticus.db.Expense
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

const val TOTAL_AMOUNT = "TOTAL_AMOUNT"
const val TOTAL_EXPENSES = "TOTAL_EXPENSES"

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainActViewModel

    companion object {
        const val ADD_REQUEST_CODE = 1
        const val EXPENSE_CATEGORY_TEXT_EXTRA = "EXPENSE_CATEGORY_TEXT_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ExpensesListAdapter(applicationContext)
        recycler_view.adapter = adapter

        mainViewModel = ViewModelProvider(this).get(MainActViewModel::class.java)

        updateTotalTexts(expensesData = mainViewModel.loadExpensesDataFromSharedPrefs())

        //mainViewModel.allExpensesInDB.launch {
        mainViewModel.allExpensesInDB.observe(this@MainActivity, Observer { exp ->
            exp?.let {
                adapter.setExpenses(it)
                updateExpensesTexts(it)
            }
        })

    }

    suspend private fun makeListOfExpensesByDay(): LiveData<List<Expense>> {
        return mainViewModel.getExpensesByDay(date_tv.text.toString())
    }

    fun expensesOnClickListener(v: View?) {//слушает нажатия на кнопки с расходами
        if (v !is Button) return
        startActivityIntent(v)
    }

    //Выполняется при закрытии активити с добалением расходов. Здесь данные должны отправляться в бд
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data ?: return

        val newExpenseAmount = data.getStringExtra(EXPENSE_AMOUNT_EXTRA)?.toInt() ?: 0

        val currentTotalAmount = total_amount_tv.text.toString().toInt()
        val currentTotalExpenses = total_expenses_tv.text.toString().toInt()

        val newTotalAmount = currentTotalAmount - newExpenseAmount
        val newTotalExpenses = currentTotalExpenses + newExpenseAmount

        total_amount_tv.text = newTotalAmount.toString()
        total_expenses_tv.text = newTotalExpenses.toString()

        val category: String? = data.getStringExtra(EXPENSE_CATEGORY_EXTRA)
        when (category) {

            PRODUCTS.description -> addProductsExpense(newExpenseAmount)
            TRANSPORT.description -> addTransportExpense(newExpenseAmount)
        }

        //взаимодействие с бд
        val expense = Expense(
                transactionId = generateTransactionID(),
                expCategory = category.toString(),
                date = date_tv.text.toString(),
                sum = newExpenseAmount
        )
        mainViewModel.insert(expense)
    }

    private fun addTransportExpense(newExp: Int) = when (transport_expenses.text) {

        "0" -> transport_expenses.text = newExp.toString()

        else -> {
            val transExp = transport_expenses.text.toString().toInt()
            val newTransExp = transExp + newExp
            transport_expenses.text = newTransExp.toString()
        }
    }

    private fun addProductsExpense(newExp: Int) = when (products_expenses.text) {

        "0" -> products_expenses.text = newExp.toString()

        else -> {
            val prodExp = products_expenses.text.toString().toInt()
            val newProdExp = prodExp + newExp
            products_expenses.text = newProdExp.toString()
        }
    }

    private fun generateTransactionID(): String = UUID.randomUUID().toString()

    private fun updateTotalTexts(expensesData: ExpensesTextHolder) {

        total_expenses_tv.text = expensesData.allExpensesText
        total_amount_tv.text = expensesData.allMoneyText
    }

    private fun updateExpensesTexts(expenses: List<Expense>) {

        date_tv.text = SimpleDateFormat("dd.MM.yy").format(Date(System.currentTimeMillis()))

        //берем из бд все расходы за текущий день по категориям
        if (expenses.isEmpty().not()) {

            val listOfProductsExp = expenses.filter { it.expCategory == PRODUCTS.description && it.date.toString() == date_tv.text.toString() }
            val listOfTransportExp = expenses.filter { it.expCategory == TRANSPORT.description && it.date.toString() == date_tv.text.toString() }

            val sumOfProductsExp = listOfProductsExp.sumBy { it.sum }
            val sumOfTransportExp = listOfTransportExp.sumBy { it.sum }

            products_expenses.text = sumOfProductsExp.toString()//textView
            transport_expenses.text = sumOfTransportExp.toString()//textView
        }
    }

    override fun onPause() {
        super.onPause()

        val expensesData = ExpensesTextHolder(

                allExpensesText = total_expenses_tv.text.toString(),
                allMoneyText = total_amount_tv.text.toString()
        )

        mainViewModel.saveExpensesDataToSharedPrefs(expensesData)
    }

    private fun startActivityIntent(categoryBtn: Button) {

        val intent = Intent(this, ActivityToAddExpense::class.java)
        val expenseCategoryText = categoryBtn.text
        intent.putExtra(EXPENSE_CATEGORY_TEXT_EXTRA, expenseCategoryText)
        startActivityForResult(intent, ADD_REQUEST_CODE)
    }


    fun funDate(v: View) {
        val c = Calendar.getInstance()
        var day = c.get(Calendar.DAY_OF_MONTH)
        var month = c.get(Calendar.MONTH + 1)
        var year = c.get(Calendar.YEAR)
            var dpd = DatePickerDialog(this@MainActivity, android.R.style.Theme_DeviceDefault_Light_Dialog,
                    DatePickerDialog.OnDateSetListener() { dp, year, month, day ->
                        mainViewModel.viewModelScope.launch {
                        makeNewDate(dp, year, month, day)
                    }}, year, month, day)
            dpd.show()
    }

    suspend fun makeNewDate(dp : DatePicker, year: Int, month: Int, day: Int){

        var monthStr = month.toString()
        var dayStr = day.toString()
        var newYear = year
        if (month < 10) {
                monthStr = "0" + (month+1).toString()
             }
            if (day < 10) {
             dayStr = "0" + day
            }
        newYear %= 100
        date_tv.text = "$dayStr.$monthStr.$newYear"

        //обновление расходов у кнопок
        var check = mainViewModel.allExpensesInDB
        updateExpensesTexts(mainViewModel.allExpensesInDB.value!!)

    }
}