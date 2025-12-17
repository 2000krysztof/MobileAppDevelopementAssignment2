package com.example.financetrackerv2.DataModels

data class User(
    val email: String = "",
    val entries: List<BudgetEntry> = emptyList()
)