package com.currencyexchanger.ui.exchanger

sealed interface ExchangerUIEvent {
    data object OnConversionCompleted : ExchangerUIEvent
}