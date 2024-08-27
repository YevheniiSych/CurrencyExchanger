package com.currencyexchanger.data.room.balance

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BalanceDao {
    @Upsert
    suspend fun upsertBalance(balance: Balance)

    @Delete
    suspend fun deleteBalance(balance: Balance)

    @Query("SELECT * FROM Balance")
    fun getBalances(): Flow<List<Balance>>
}