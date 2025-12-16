package com.example.financetrackerv2.Screens
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Settings : Screen("settings")
    object BudgetList : Screen("budget list")
}