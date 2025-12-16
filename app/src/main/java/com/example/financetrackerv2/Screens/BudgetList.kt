package com.example.financetrackerv2.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.financetrackerv2.ui.components.NavBar

@Composable
fun BudgetListScreen(
    setScreen:(String)->Unit,
    currentScreen: String
){
    Scaffold(bottomBar = {
        NavBar(
            setScreen = { route->setScreen(route)},
            currentScreen
        )}
    ) {padding ->
        Box(modifier = Modifier.padding(padding)) {
            Text("Budget List")
        }
    }
}