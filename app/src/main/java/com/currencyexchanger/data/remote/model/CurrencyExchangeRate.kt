package com.currencyexchanger.data.remote.model


import com.google.gson.annotations.SerializedName

typealias ExchangeRates = Map<String, Double>

data class CurrencyExchangeRate(
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: ExchangeRates
) {
    companion object {
        val EMPTY = CurrencyExchangeRate(
            "",
            "",
            emptyMap()
        )
    }
}