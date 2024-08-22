package com.currencyexchanger.ui.exchanger

import com.currencyexchanger.data.remote.model.CurrencyExchangeRate

data class ExchangerState(
    val selectedCurrencyRate: CurrencyExchangeRate = CurrencyExchangeRate.EMPTY
)