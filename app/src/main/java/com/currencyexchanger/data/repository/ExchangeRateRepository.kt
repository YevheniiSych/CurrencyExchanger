package com.currencyexchanger.data.repository

import com.currencyexchanger.data.remote.api.ExchangeRatesApi
import com.currencyexchanger.data.remote.model.CurrencyExchangeRate
import com.currencyexchanger.data.room.ExchangerDatabase
import com.currencyexchanger.data.room.balance.Balance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

interface ExchangeRateRepository {
    fun getRatesByCurrency(): Flow<CurrencyExchangeRate>
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

    override fun getRatesByCurrency(): Flow<CurrencyExchangeRate> = flow {
        while (coroutineContext.isActive) {
            try {
                val currencyRates = api.getRatesByCurrency()
                emit(currencyRates)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            delay(5000)
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