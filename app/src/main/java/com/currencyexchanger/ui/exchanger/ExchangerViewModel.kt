package com.currencyexchanger.ui.exchanger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currencyexchanger.data.repository.ExchangeRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangerViewModel @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(ExchangerState())
    val stateFlow: StateFlow<ExchangerState> = _stateFlow

    init {
        getRatesByCurrency()
    }

    fun onEvent(event: ExchangerEvent) {

    }

    private fun getRatesByCurrency() {
        viewModelScope.launch {
            val currencyRate =
                exchangeRateRepository.getRatesByCurrency().getOrNull() ?: return@launch
            _stateFlow.update {
                it.copy(
                    selectedCurrencyRate = currencyRate
                )
            }
        }
    }
}