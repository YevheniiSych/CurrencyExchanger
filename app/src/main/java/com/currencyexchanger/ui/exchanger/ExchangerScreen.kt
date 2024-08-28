package com.currencyexchanger.ui.exchanger

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.currencyexchanger.R
import com.currencyexchanger.ui.exchanger.components.ExchangeRow
import com.currencyexchanger.ui.theme.PrimaryBlue

@Composable
fun ExchangerScreen(
    state: ExchangerState,
    onEvent: (ExchangerEvent) -> Unit
) {

    val horizontalPadding = 10.dp

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.currency_converter),
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = PrimaryBlue
                )
                .padding(top = 40.dp, bottom = 20.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.my_balances).uppercase(),
            color = Color.LightGray,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(horizontal = horizontalPadding)
        )

        LazyRow(
            modifier = Modifier.padding(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(
                items = state.balances,
                key = { it.currency }
            ) { balance ->
                Text(
                    text = "${balance.amount} ${balance.currency}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W900
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.currency_exchange).uppercase(),
            color = Color.LightGray,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(horizontal = horizontalPadding)
        )

        Spacer(modifier = Modifier.height(10.dp))

        ExchangeRow(
            currencies = state.currenciesToSell,
            icon = painterResource(R.drawable.sell_icon),
            text = stringResource(R.string.sell),
            currency = state.selectedCurrencyToSell ?: "",
            amount = state.amountToSellInput,
            onAmountChange = {
                onEvent(ExchangerEvent.OnSellAmountInput(it))
            },
            onCurrencySelected = {
                onEvent(ExchangerEvent.OnCurrencyToSellChanged(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
        )

        Spacer(modifier = Modifier.height(10.dp))

        ExchangeRow(
            currencies = state.currenciesToBuy,
            icon = painterResource(R.drawable.buy_icon),
            text = stringResource(R.string.buy),
            currency = state.selectedCurrencyToBuy ?: "",
            amount = state.amountToBuyInput,
            onAmountChange = {
                onEvent(ExchangerEvent.OnBuyAmountInput(it))
            },
            onCurrencySelected = {
                onEvent(ExchangerEvent.OnCurrencyToBuyChanged(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                onEvent(ExchangerEvent.OnSubmitExchange)
            },
            colors = ButtonColors(
                contentColor = Color.White,
                containerColor = PrimaryBlue,
                disabledContentColor = Color.Gray,
                disabledContainerColor = Color.DarkGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
        ) {
            Text(text = "Submit")
        }
    }
}


@Composable
@Preview
private fun ExchangerScreenPreview() {
    ExchangerScreen(state = ExchangerState()) {}
}