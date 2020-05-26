package com.example.proecticus.data

/** Через него сохраняются и подгружаются
 * текстовые поля с помощью Shared Prefs
 */
data class ExpensesTextHolder(
    val allExpensesText: String,
    val allMoneyText: String
)