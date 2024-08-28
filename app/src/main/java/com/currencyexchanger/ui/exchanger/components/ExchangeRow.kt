package com.currencyexchanger.ui.exchanger.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExchangeRow(
    currencies: List<String>,
    icon: Painter,
    text: String,
    currency: String,
    amount: String,
    onAmountChange: (amount: String) -> Unit,
    onCurrencySelected: (currency: String) -> Unit,
    modifier: Modifier = Modifier,
) {

    var dropdownMenuExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = "Sell icon",
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            fontSize = 16.sp,
        )

        BasicTextField(
            value = amount,
            onValueChange = {
                onAmountChange(it)
            },
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(
                textAlign = TextAlign.End,
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
            ),
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal
            )
        )

        Spacer(modifier = Modifier.width(10.dp))

        Row(
            modifier = Modifier.clickable {
                dropdownMenuExpanded = true
            }
        ) {
            Text(text = currency)

            Icon(
                imageVector = if (dropdownMenuExpanded) Icons.Default.KeyboardArrowUp
                else Icons.Default.KeyboardArrowDown,
                contentDescription = "Dropdown icon"
            )

            DropdownMenu(
                expanded = dropdownMenuExpanded,
                onDismissRequest = { dropdownMenuExpanded = false },
            ) {
                currencies.forEach { currency ->
                    DropdownMenuItem(text = {
                        Text(text = currency)
                    }, onClick = {
                        dropdownMenuExpanded = false
                        onCurrencySelected(currency)
                    })
                }
            }
        }
    }

}