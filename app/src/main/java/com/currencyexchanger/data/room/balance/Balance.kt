package com.currencyexchanger.data.room.balance

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Balance"
)
data class Balance(
    @PrimaryKey
    @ColumnInfo(name = "currency", defaultValue = "")
    val currency: String,
    @ColumnInfo(name = "amount", defaultValue = "0.0")
    val amount: Double
)
