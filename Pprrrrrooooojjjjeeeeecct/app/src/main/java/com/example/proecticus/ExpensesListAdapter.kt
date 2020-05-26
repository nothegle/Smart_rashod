package com.example.proecticus

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpensesListAdapter internal constructor(
        context: Context
) : RecyclerView.Adapter<ExpensesListAdapter.ExpensesViewHolder>() {


        private val inflater: LayoutInflater = LayoutInflater.from(context)
        private var expenses = emptyList<Expense>() // Cached copy of words

        inner class ExpensesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val expensesItemView: TextView = itemView.findViewById(R.id.textView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesViewHolder {
            val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
            return ExpensesViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int) {
            val current = expenses[position]
            holder.expensesItemView.text = current.expCategory
        }

        internal fun setExpenses(expens: List<Expense>) {
            this.expenses = expens
            notifyDataSetChanged()
        }

        override fun getItemCount() = expenses.size

}