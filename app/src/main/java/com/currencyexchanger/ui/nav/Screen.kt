package com.currencyexchanger.ui.nav

sealed class Screen(val route: String) {
    data object Exchanger: Screen("exchanger_screen")
}