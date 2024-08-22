package com.currencyexchanger.data.repository

import com.currencyexchanger.data.remote.api.ExchangeRatesApi
import com.currencyexchanger.data.remote.model.CurrencyExchangeRate

interface ExchangeRateRepository {
    suspend fun getRatesByCurrency(): Result<CurrencyExchangeRate>
}

fun createExchangeRateRepository(api: ExchangeRatesApi): ExchangeRateRepository {
    return ExchangeRateRepositoryImpl(api)
}

private class ExchangeRateRepositoryImpl(private val api: ExchangeRatesApi) :
    ExchangeRateRepository {

    override suspend fun getRatesByCurrency(): Result<CurrencyExchangeRate> {
        return try {
            val currencyRates = api.getRatesByCurrency()
            Result.success(currencyRates)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}