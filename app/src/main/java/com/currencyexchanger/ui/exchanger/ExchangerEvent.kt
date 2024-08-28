package com.currencyexchanger.ui.exchanger

sealed interface ExchangerEvent {
    data class OnSellAmountInput(val amount: String) : ExchangerEvent
    data class OnCurrencyToSellChanged(val currency: String) : ExchangerEvent
    data class OnBuyAmountInput(val amount: String) : ExchangerEvent
    data class OnCurrencyToBuyChanged(val currency: String) : ExchangerEvent
    data object OnSubmitExchange: ExchangerEvent
}