package com.example.financetrackerv2.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.financetrackerv2.BudgetEntryUi
import com.example.financetrackerv2.DataModels.BudgetEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteEntryDialogue(
    budgetEntry: BudgetEntryUi,
    deleteEntry : (BudgetEntryUi)->Unit,
    hide : ()-> Unit){
    AlertDialog(
        onDismissRequest = hide,
        title = {
            Text("Delete entry")
        },
        text = {
            Text("Are you sure you want to delete ${budgetEntry.entry.title}?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    deleteEntry(budgetEntry)
                    hide()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = hide) {
                Text("Cancel")
            }
        }
    )
}