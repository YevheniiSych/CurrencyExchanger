package com.currencyexchanger.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.currencyexchanger.ui.exchanger.ExchangerScreen
import com.currencyexchanger.ui.exchanger.ExchangerViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Exchanger.route
    ) {
        composable(route = Screen.Exchanger.route) {
            val viewModel = hiltViewModel<ExchangerViewModel>()
            val exchangerState = viewModel.stateFlow.collectAsState()
            ExchangerScreen(
                state = exchangerState.value,
                onEvent = viewModel::onEvent
            )
        }
    }
}