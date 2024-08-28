package com.currencyexchanger.ui.exchanger

import com.currencyexchanger.data.remote.model.CurrencyExchangeRate
import com.currencyexchanger.data.room.balance.Balance

data class ExchangerState(
    val selectedCurrencyRates: CurrencyExchangeRate = CurrencyExchangeRate.EMPTY, // move to ViewModel
    val balances: List<Balance> = emptyList(),
    val selectedCurrencyToSell: String? = null,
    val amountToSellInput: String = "100.0",
    val selectedCurrencyToBuy: String? = null,
    val amountToBuyInput: String = "100.0",
) {
    val currenciesToSell: List<String>
        get() = balances.map { it.currency }.filter { it in selectedCurrencyRates.rates.keys }

    val currenciesToBuy: List<String>
        get() = selectedCurrencyRates.rates.keys.toList()
}