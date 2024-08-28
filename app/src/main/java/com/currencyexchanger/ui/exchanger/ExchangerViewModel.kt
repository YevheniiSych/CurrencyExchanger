package com.currencyexchanger.ui.exchanger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currencyexchanger.data.remote.model.ExchangeRates
import com.currencyexchanger.data.repository.ExchangeRateRepository
import com.currencyexchanger.data.room.balance.Balance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangerViewModel @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(ExchangerState())
    val stateFlow: StateFlow<ExchangerState> = _stateFlow

    private val currencyRates: ExchangeRates
        get() = stateFlow.value.selectedCurrencyRates.rates

    private val selectedCurrencyToBuy: String?
        get() = stateFlow.value.selectedCurrencyToBuy

    private val selectedCurrencyToSell: String?
        get() = stateFlow.value.selectedCurrencyToSell

    private val amountToSell: Double?
        get() = stateFlow.value.amountToSellInput.toDoubleOrNull()

    private val amountToBuy: Double?
        get() = stateFlow.value.amountToBuyInput.toDoubleOrNull()

    private val currencyRateToBuy: Double?
        get() = selectedCurrencyToBuy?.let { getRateByCurrency(it) }

    private val currencyRateToSell: Double?
        get() = selectedCurrencyToSell?.let { getRateByCurrency(it) }

    init {
        getAllBalances()
        getRatesByCurrency()
    }

    fun onEvent(event: ExchangerEvent) {
        when (event) {
            is ExchangerEvent.OnBuyAmountInput -> {
                val amountToBuy = event.amount.toDoubleOrNull() ?: 0.0
                _stateFlow.update {
                    it.copy(
                        amountToBuyInput = event.amount
                    )
                }
                updateCurrencyToSellAmount(amountToBuy)
            }

            is ExchangerEvent.OnCurrencyToBuyChanged -> {
                _stateFlow.update {
                    it.copy(
                        selectedCurrencyToBuy = event.currency
                    )
                }
                updateCurrencyToSellAmount(amountToBuy ?: 0.0)
            }

            is ExchangerEvent.OnCurrencyToSellChanged -> {
                _stateFlow.update {
                    it.copy(
                        selectedCurrencyToSell = event.currency
                    )
                }
                updateCurrencyToBuyAmount(amountToSell ?: 0.0)
            }

            is ExchangerEvent.OnSellAmountInput -> {
                val amountToSell = event.amount.toDoubleOrNull() ?: 0.0
                _stateFlow.update {
                    it.copy(
                        amountToSellInput = event.amount
                    )
                }
                updateCurrencyToBuyAmount(amountToSell)
            }

            ExchangerEvent.OnSubmitExchange -> {
                submitExchange()
            }
        }
    }

    private fun updateCurrencyToBuyAmount(amountToSell: Double) {
        val amountToBuy = getCurrencyAmountToBuy(
            currencyRateToBuy,
            currencyRateToSell,
            amountToSell
        ) ?: return
        _stateFlow.update {
            it.copy(
                amountToBuyInput = amountToBuy.toString()
            )
        }
    }

    private fun updateCurrencyToSellAmount(amountToBuy: Double) {
        val amountToSell = getCurrencyAmountToSell(
            currencyRateToBuy,
            currencyRateToSell,
            amountToBuy
        ) ?: return
        _stateFlow.update {
            it.copy(
                amountToSellInput = amountToSell.toString()
            )
        }
    }

    private fun submitExchange() {
        viewModelScope.launch {
            val amountToSell = amountToSell ?: return@launch
            val currencyToBuy = selectedCurrencyToBuy ?: return@launch
            val amountToBuy = amountToBuy ?: return@launch
            val currencyToSell = selectedCurrencyToSell ?: return@launch
            val currentBalanceToSellCurrency =
                stateFlow.value.balances.firstOrNull { it.currency == currencyToSell }
                    ?: return@launch
            val currentBalanceAmountToBuyCurrency =
                stateFlow.value.balances.firstOrNull { it.currency == currencyToBuy }?.amount
                    ?: 0.0
            if (currentBalanceToSellCurrency.amount < amountToSell) {
                return@launch
            }
            val newBalanceToSell = Balance(
                currencyToSell,
                currentBalanceToSellCurrency.amount - amountToSell
            )
            val newBalanceToBuy = Balance(
                currencyToBuy,
                amountToBuy + currentBalanceAmountToBuyCurrency
            )
            exchangeRateRepository.run {
                listOf(
                    async {
                        upsertBalance(newBalanceToSell)
                    },
                    async {
                        upsertBalance(newBalanceToBuy)
                    }
                )
            }
        }
    }

    private fun getAllBalances() {
        exchangeRateRepository.getAllBalances()
            .onEach { balances ->
                val balancesToShow = balances
                    .filter { it.amount > 0 }
                    .sortedByDescending { it.amount }
                _stateFlow.update { state ->
                    //Show currency to sell with the biggest wallet balance
                    val currencyToSell = if (stateFlow.value.selectedCurrencyToSell == null) {
                        balancesToShow.getOrNull(0)?.currency ?: ""
                    } else {
                        state.selectedCurrencyToSell
                    }
                    state.copy(
                        balances = balances,
                        selectedCurrencyToSell = currencyToSell
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun getRatesByCurrency() {
        exchangeRateRepository.getRatesByCurrency().onEach { currencyRates ->
            val selectedCurrencyToBuy = if (stateFlow.value.selectedCurrencyToBuy == null) {
                currencyRates.rates.keys.firstOrNull() ?: ""
            } else {
                stateFlow.value.selectedCurrencyToBuy
            }

            _stateFlow.update {
                it.copy(
                    selectedCurrencyRates = currencyRates,
                    selectedCurrencyToBuy = selectedCurrencyToBuy
                )
            }

            updateCurrencyToBuyAmount(amountToSell ?: return@onEach)
        }.launchIn(viewModelScope)
    }

    private fun getRateByCurrency(currency: String): Double? {
        return currencyRates.getOrElse(currency) {
            return null
        }
    }

    private fun getCurrencyAmountToBuy(
        currencyRateToBuy: Double?,
        currencyRateToSell: Double?,
        currencyToSellAmount: Double?
    ): Double? {
        if (currencyToSellAmount == null || currencyRateToBuy == null || currencyRateToSell == null) {
            return null
        }
        val currencyToSellInBase = currencyToSellAmount / currencyRateToSell
        return currencyToSellInBase * currencyRateToBuy
    }

    private fun getCurrencyAmountToSell(
        currencyRateToBuy: Double?,
        currencyRateToSell: Double?,
        currencyToBuyAmount: Double?
    ): Double? {
        if (currencyToBuyAmount == null || currencyRateToBuy == null || currencyRateToSell == null) {
            return null
        }
        return currencyToBuyAmount / currencyRateToBuy * currencyRateToSell
    }
}