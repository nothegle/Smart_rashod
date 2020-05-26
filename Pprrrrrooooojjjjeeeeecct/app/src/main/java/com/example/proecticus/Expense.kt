package com.example.proecticus

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(tableName = "expenses_table")
data class Expense(

    @PrimaryKey
    @ColumnInfo(name = "transactionID")
    val transactionId: String,

    @ColumnInfo(name = "expenseCategory")
    val expCategory: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "sum")
    val sum: Int
)
