package com.currencyexchanger.data.remote.api

import com.currencyexchanger.data.remote.model.CurrencyExchangeRate
import retrofit2.http.GET

interface ExchangeRatesApi {
    @GET("/tasks/api/currency-exchange-rates")
    suspend fun getRatesByCurrency(): CurrencyExchangeRate //currency is always EUR due to backend restrictions
}