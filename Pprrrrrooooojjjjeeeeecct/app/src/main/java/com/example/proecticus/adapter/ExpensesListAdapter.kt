package com.example.proecticus.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proecticus.R
import com.example.proecticus.db.Expense

class ExpensesListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<ExpensesListAdapter.ExpensesViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var expenses = emptyList<Expense>() // Cached copy of words

    inner class ExpensesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expensesItemView: TextView = itemView.findViewById(
            R.id.all_amount_tv
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return ExpensesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int) {
        val current = expenses[position]
        holder.expensesItemView.text = "${current.expCategory} | ${current.sum} | ${current.date}"
    }

    fun setExpenses(expense: List<Expense>) {
        this.expenses = expense
        notifyDataSetChanged()
    }

    override fun getItemCount() = expenses.size
}