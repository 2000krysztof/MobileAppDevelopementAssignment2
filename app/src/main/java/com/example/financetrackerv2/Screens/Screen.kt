package com.example.financetrackerv2.Screens
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
}