package com.currencyexchanger.ui.exchanger

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ExchangerScreen(
    state: ExchangerState,
    onEvent: (ExchangerEvent) -> Unit
) {

}

@Composable
@Preview
private fun ExchangerScreenPreview() {
    ExchangerScreen(state = ExchangerState()) {

    }
}