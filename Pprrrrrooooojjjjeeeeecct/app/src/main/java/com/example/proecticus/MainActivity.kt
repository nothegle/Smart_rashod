package com.example.proecticus

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proecticus.ActivityToAddExpense.Companion.EXPENSE_AMOUNT_EXTRA
import com.example.proecticus.ActivityToAddExpense.Companion.EXPENSE_CATEGORY_EXTRA
import com.example.proecticus.ActivityToAddMoney.Companion.TOTAL_AMOUNT_EXTRA
import com.example.proecticus.ActivityToLimitToExpense.Companion.ADD_LIMIT_EXTRA
import com.example.proecticus.adapter.ExpensesListAdapter
import com.example.proecticus.data.ExpenseCategory.*
import com.example.proecticus.data.ExpensesTextHolder
import com.example.proecticus.db.Expense
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


const val TOTAL_AMOUNT = "TOTAL_AMOUNT"
const val TOTAL_EXPENSES = "TOTAL_EXPENSES"
const val TOTAL_LIMITS = "TOTAL_LIMITS"
private val CHANNEL_ID = "Notification_chanel"
private val NOTIFY_ID = 5


class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainActViewModel
    private val dateFilter = MutableLiveData(getCurrentDate())
    private val adapter by lazy { ExpensesListAdapter(applicationContext) }

    /**
     * нужен, чтобы можно было апдейтить текста из любого места,
     * так как список транзакций всегда выгружен и хранится тут
     */
    private val expenses = mutableListOf<Expense>()

    companion object {
        const val ADD_REQUEST_CODE = 1
        const val ADD_TOTAL_REQUEST_CODE = 2
        const val ADD_LIMIT_REQUEST_CODE = 3
        const val EXPENSE_CATEGORY_TEXT_EXTRA = "EXPENSE_CATEGORY_TEXT_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.adapter = adapter

        mainViewModel = ViewModelProvider(this).get(MainActViewModel::class.java)

        date_tv.text = dateFilter.value
        updateTotalTexts(expensesData = mainViewModel.loadExpensesDataFromSharedPrefs())

        mainViewModel.allExpensesInDB.observe(this@MainActivity, Observer { exp ->
            exp?.let {
                adapter.setExpenses(it)

                /**
                 * при изменении значений в бд изменяется список, который хранится в этой активтии
                 */
                expenses.clear()
                expenses.addAll(it)
                updateExpensesTexts()
            }
        })

        /**
         * при изменении даты изменяется текстовое поле с датой
         */
        dateFilter.observe(this, Observer { date -> date_tv.text = date })
    }

    fun dropData(v: View?){
        mainViewModel.viewModelScope.launch {
            async {clearData()}.await()
        }
        expenses.clear()
        total_amount_tv.text = "0"
        total_expenses_tv.text = "0"
    }

    suspend fun clearData(){
        mainViewModel.clearData()
    }

    fun expensesOnClickListener(v: View?) {//слушает нажатия на кнопки с расходами
        if (v !is Button) return
        startActivityIntent(v)
    }

    fun onAddMoneyBtnClick(v : View?){
        if (v !is Button) return
        var intent = Intent(this, ActivityToAddMoney::class.java)
        startActivityForResult(intent, ADD_TOTAL_REQUEST_CODE)
    }

    //Выполняется при закрытии активити с добалением расходов. Здесь данные должны отправляться в бд
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data ?: return

        if(requestCode == ADD_REQUEST_CODE) {
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
                PRESENTS.description -> addPresentsExpenses(newExpenseAmount)
                CAFE.description -> addCafeExpense(newExpenseAmount)
                HEALTH.description -> addHeathExpense(newExpenseAmount)
                RECREATION.description -> addRecreationExpense(newExpenseAmount)
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



        if (requestCode == ADD_TOTAL_REQUEST_CODE){// ДОБАВЛЕНИЕ ДЕНЕГ
            val newMoneyAmount = data.getStringExtra(TOTAL_AMOUNT_EXTRA)?.toInt() ?: 0

            val currentTotalAmount = total_amount_tv.text.toString().toInt()

            val newTotalAmount = currentTotalAmount + newMoneyAmount
            total_amount_tv.text = newTotalAmount.toString()
        }

        if (requestCode == ADD_LIMIT_REQUEST_CODE){
            val newLimit = data.getStringExtra(ADD_LIMIT_EXTRA)?.toInt() ?: 0
            limit_tv.text = newLimit.toString()
        }

                //checkLimitToExpenses()

    }

    private  fun addPresentsExpenses(newExp: Int) = when (presents_expenses.text){
        "0" -> presents_expenses.text = newExp.toString()

        else -> {
            val presentsExp = presents_expenses.text.toString().toInt()
            val newPresentsExp = presentsExp + newExp
            presents_expenses.text = newPresentsExp.toString()
        }
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

    private fun addHeathExpense(newExp: Int) = when (health_expenses.text){
        "0" -> health_expenses.text = newExp.toString()

        else -> {
            val healthExp = health_expenses.text.toString().toInt()
            val newHealthExp = healthExp + newExp
            health_expenses.text = newHealthExp.toString()
        }
    }

    private fun addRecreationExpense(newExp: Int) = when (recreation_expenses.text){
        "0" -> recreation_expenses.text = newExp.toString()

        else -> {
            val recreationExp = recreation_expenses.text.toString().toInt()
            val newRecreationExp = recreationExp + newExp
            recreation_expenses.text = newRecreationExp.toString()
        }
    }

    private fun addCafeExpense(newExp: Int) = when (cafe_expenses.text){
        "0" -> cafe_expenses.text = newExp.toString()

        else -> {
            val cafeExp = cafe_expenses.text.toString().toInt()
            val newCafeExp = cafeExp + newExp
            cafe_expenses.text = newCafeExp.toString()
        }
    }

    private fun generateTransactionID(): String = UUID.randomUUID().toString()

    private fun updateTotalTexts(expensesData: ExpensesTextHolder) {
        total_expenses_tv.text = expensesData.allExpensesText
        total_amount_tv.text = expensesData.allMoneyText
        limit_tv.text = expensesData.limitText
    }

    private fun updateExpensesTexts() {

        //берем из бд все расходы за текущий день по категориям
        if (expenses.isEmpty().not()) {

            val productExpenses = expenses.filter {
                it.expCategory == PRODUCTS.description && it.date == dateFilter.value
            }
            val transportExpenses = expenses.filter {
                it.expCategory == TRANSPORT.description && it.date == dateFilter.value
            }
            val presentsExpenses = expenses.filter {
                it.expCategory == PRESENTS.description && it.date == dateFilter.value
            }
            val cafeExpenses = expenses.filter {
                it.expCategory == CAFE.description && it.date == dateFilter.value
            }
            val healthExpenses = expenses.filter {
                it.expCategory == HEALTH.description && it.date == dateFilter.value
            }
            val recreationExpenses = expenses.filter {
                it.expCategory == RECREATION.description && it.date == dateFilter.value
            }
            val sumOfProductsExp = productExpenses.sumBy { it.sum }
            val sumOfTransportExp = transportExpenses.sumBy { it.sum }
            val sumOfPresentsExp = presentsExpenses.sumBy { it.sum }
            val sumOfCafeExp = cafeExpenses.sumBy { it.sum }
            val sumOfHealthExp = healthExpenses.sumBy { it.sum }
            val sumOfRecreationExp = recreationExpenses.sumBy { it.sum }

            products_expenses.text = sumOfProductsExp.toString()//textView
            transport_expenses.text = sumOfTransportExp.toString()//textView
            presents_expenses.text = sumOfPresentsExp.toString()
            cafe_expenses.text = sumOfCafeExp.toString()
            health_expenses.text = sumOfHealthExp.toString()
            recreation_expenses.text = sumOfRecreationExp.toString()

            checkLimitToExpenses()
            /**
             * сейчас изменяются лишь products_expenses и transport_expenses
             * если надо изменять какие-то еще поля - нужно их добавить в этот метод
             */

            /**
             * если надо обновлять список по дате - делаешь тут
             * но тогда надо учитывать, что отображается всегда по установленной дате
             */
//            adapter.setExpenses(expenses.filter { it.date == dateFilter.value })
        }
    }

    override fun onPause() {
        super.onPause()

        val expensesData = ExpensesTextHolder(

            allExpensesText = total_expenses_tv.text.toString(),
            allMoneyText = total_amount_tv.text.toString(),
                limitText = limit_tv.text.toString()
        )

        mainViewModel.saveExpensesDataToSharedPrefs(expensesData)
    }

    private fun startActivityIntent(categoryBtn: Button) {

        val intent = Intent(this, ActivityToAddExpense::class.java)
        val expenseCategoryText = categoryBtn.text
        intent.putExtra(EXPENSE_CATEGORY_TEXT_EXTRA, expenseCategoryText)
        startActivityForResult(intent, ADD_REQUEST_CODE)
    }

    fun changeDateClick(v: View) {

        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH + 1)
        val year = c.get(Calendar.YEAR) - 1
        val dpd = DatePickerDialog(
            this@MainActivity,
            android.R.style.Theme_DeviceDefault_Light_Dialog,
            DatePickerDialog.OnDateSetListener { dp, dpYear, dpMonth, dpDay ->
                mainViewModel.viewModelScope.launch {
                    makeNewDate(dp, dpYear, dpMonth, dpDay)
                }
            },
            year,
            month,
            day
        )

        dpd.show()
    }

    fun makeNewDate(dp: DatePicker, year: Int, month: Int, day: Int) {

        var monthStr = month.toString()
        var dayStr = day.toString()
        var newYear = year
        if (month < 10) {
            monthStr = "0${(month + 1)}"
        }
        if (day < 10) {
            dayStr = "0$day"
        }
        newYear %= 100
        dateFilter.value = "$dayStr.$monthStr.$newYear"
        updateExpensesTexts()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate() = SimpleDateFormat("dd.MM.yy").format(Date(System.currentTimeMillis()))

    fun addLimitToExpense(view: View){
        val intent = Intent(this, ActivityToLimitToExpense::class.java)
        startActivityForResult(intent, ADD_LIMIT_REQUEST_CODE)
    }


    private fun checkLimitToExpenses() {
        if (expenses.isEmpty().not()) {
            val expensesByDay = expenses.filter {
                it.date == dateFilter.value
            }
            val sumExpByDay = expensesByDay.sumBy { it.sum }



            if(sumExpByDay > limit_tv.text.toString().toInt()){

                val ringURI: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

                var builder = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
                        .setContentTitle("Внимание")
                        .setContentText("Вы превысили свои расходы!")
                        .setSmallIcon(R.mipmap.baseline)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSound(ringURI)
                        .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                        .build()




                var notificationManager = NotificationManagerCompat.from(this@MainActivity)
                notificationManager.notify(NOTIFY_ID, builder)



                var toast = Toast.makeText(this,"Вы превысили свои расходы!",Toast.LENGTH_LONG )
                toast.show()
            }
        }
    }
}