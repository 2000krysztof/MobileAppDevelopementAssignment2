package com.example.financetrackerv2.DataModels

import com.google.firebase.Timestamp

data class BudgetEntry(
    val title: String = "",
    val description: String = "",
    val timestamp: Timestamp? = null,
    val amount : Double = 0.0
)