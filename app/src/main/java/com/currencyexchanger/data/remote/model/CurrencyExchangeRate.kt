package com.currencyexchanger.data.remote.model


import com.google.gson.annotations.SerializedName

data class CurrencyExchangeRate(
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: Map<String, Double>
) {
    companion object {
        val EMPTY = CurrencyExchangeRate(
            "",
            "",
            emptyMap()
        )
    }
}