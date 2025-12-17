package com.example.financetrackerv2.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import com.example.financetrackerv2.DataModels.BudgetEntry
import com.google.firebase.Timestamp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBudgetEntryDialogue(addBudgetEntry : (String, String, Timestamp, Double)-> Unit,
                           hide:()-> Unit){
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableDoubleStateOf(0.0) }
    var timestamp by remember { mutableStateOf(Timestamp.now()) }

    ModalBottomSheet(
        onDismissRequest = hide
    ) {
        Text("Add budget entry")


        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )
        TextField(
            value = amount.toString(),
            onValueChange = {
                try{
                    amount = it.toDouble()
                }catch (e: Exception){}
                            },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        DatePickerComponent(
            onDateSelected = {time ->
                timestamp = time
            }
        )

        Button (onClick = {
            addBudgetEntry(title,description,timestamp,amount)
            hide()
            title = ""
            description = ""
            amount = 0.0
            timestamp = Timestamp.now()
        }) {
            Text("Done")
        }
    }
}