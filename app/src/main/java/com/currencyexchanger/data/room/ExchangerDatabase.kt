package com.currencyexchanger.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.currencyexchanger.data.room.balance.Balance
import com.currencyexchanger.data.room.balance.BalanceDao

@Database(
    entities = [
        Balance::class
    ],
    version = 1
)
abstract class ExchangerDatabase: RoomDatabase() {

    abstract val balanceDao: BalanceDao

    companion object {
        const val DATABASE_NAME = "exchanger_db"
    }
}