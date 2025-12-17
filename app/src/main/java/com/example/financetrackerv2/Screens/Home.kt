package com.example.financetrackerv2.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.financetrackerv2.BudgetEntryUi
import com.example.financetrackerv2.ui.components.AddBudgetEntryDialogue
import com.example.financetrackerv2.ui.components.BudgetEntryListView
import com.example.financetrackerv2.ui.components.DeleteEntryDialogue
import com.example.financetrackerv2.ui.components.EditEntryDialogue
import com.example.financetrackerv2.ui.components.NavBar
import com.google.firebase.Timestamp

@Composable
fun HomeScreen(
    setScreen:(String)->Unit,
    currentScreen: String,
    addBudgetEntry: (String, String, Timestamp, Double)->Unit,
    deleteBudgetEntry: (BudgetEntryUi) -> Unit,
    editBudgetEntry: (BudgetEntryUi) -> Unit,
    entries:List<BudgetEntryUi>
){

    var showAddEntry by remember { mutableStateOf(false)}
    var showDeleteEntry by remember { mutableStateOf(false)}
    var showEditEntry by remember { mutableStateOf(false) }
    var targetDeleteEntry : BudgetEntryUi? = null
    var targetEditEntry : BudgetEntryUi? = null

    Scaffold(
        bottomBar = {
            NavBar(
                setScreen = { route->setScreen(route)},
                currentScreen
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showAddEntry = true
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Budget Entry"
                )
            }
        }
    ) {padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text("Home")
            BudgetEntryListView(entries,
                deleteEntry = { entryUi ->
                    showDeleteEntry = true
                    targetDeleteEntry = entryUi
            },
                editEntry = {entryUi ->
                    showEditEntry = true
                    targetEditEntry = entryUi
                }
            )



            if(showDeleteEntry && targetDeleteEntry!=null){
                DeleteEntryDialogue(
                    targetDeleteEntry,
                    deleteEntry = { entry ->
                        deleteBudgetEntry(entry)
                    },
                    hide = {
                        showDeleteEntry = false
                    }
                    )
            }
            if(showEditEntry && targetEditEntry != null){
                EditEntryDialogue(
                    targetEditEntry,
                    editEntry = editBudgetEntry,
                    hide = {showEditEntry = false}
                )
            }

            if(showAddEntry){
                AddBudgetEntryDialogue(
                    addBudgetEntry = addBudgetEntry,
                    hide = {showAddEntry = false}
                )
            }

        }
    }
}