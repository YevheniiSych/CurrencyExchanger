package com.currencyexchanger.data.repository

import com.currencyexchanger.data.remote.api.ExchangeRatesApi
import com.currencyexchanger.data.remote.model.CurrencyExchangeRate
import com.currencyexchanger.data.room.ExchangerDatabase
import com.currencyexchanger.data.room.balance.Balance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface ExchangeRateRepository {
    suspend fun getRatesByCurrency(): Result<CurrencyExchangeRate>
    fun getAllBalances(): Flow<List<Balance>>
    suspend fun upsertBalance(balance: Balance)
    suspend fun deleteBalance(balance: Balance)
}

fun createExchangeRateRepository(
    api: ExchangeRatesApi,
    database: ExchangerDatabase
): ExchangeRateRepository {
    return ExchangeRateRepositoryImpl(api, database)
}

private class ExchangeRateRepositoryImpl(
    private val api: ExchangeRatesApi,
    private val database: ExchangerDatabase
) :
    ExchangeRateRepository {

    override suspend fun getRatesByCurrency(): Result<CurrencyExchangeRate> {
        return try {
            val currencyRates = api.getRatesByCurrency()
            Result.success(currencyRates)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllBalances(): Flow<List<Balance>> {
        return database.balanceDao.getBalances()
    }

    override suspend fun upsertBalance(balance: Balance) = withContext(Dispatchers.IO) {
        try {
            database.balanceDao.upsertBalance(balance)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteBalance(balance: Balance) = withContext(Dispatchers.IO) {
        try {
            database.balanceDao.deleteBalance(balance)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}