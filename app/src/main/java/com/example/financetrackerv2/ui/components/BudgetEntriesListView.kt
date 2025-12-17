package com.example.financetrackerv2.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.financetrackerv2.BudgetEntryUi
import com.example.financetrackerv2.DataModels.BudgetEntry

@Composable
fun BudgetEntryListView(
    entries:List<BudgetEntryUi>,
    deleteEntry: (BudgetEntryUi)->Unit,
    editEntry: (BudgetEntryUi)->Unit
){
    LazyColumn {
        items(
            items = entries,
            key = { it.id }
        ) { entry ->
            BudgetEntryListItem(entry,
                deleteEntry={
                deleteEntry(entry)
            },
                editEntry={
                editEntry(entry)
                }

            )
        }
    }


}

@Composable
fun BudgetEntryListItem(
    entryUi: BudgetEntryUi,
    deleteEntry : ()->Unit,
    editEntry: ()->Unit
){
    val entry = entryUi.entry
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column {
                Text(text = entry.title)
                Text(text = entry.description)
                Text(text = entry.amount.toString())
            }
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Button(onClick = deleteEntry) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
                Button(onClick = editEntry) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
            }

        }


    }
}