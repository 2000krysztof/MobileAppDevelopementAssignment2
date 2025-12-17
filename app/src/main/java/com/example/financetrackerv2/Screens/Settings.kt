package com.example.financetrackerv2.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.financetrackerv2.ui.components.NavBar


@Composable
fun SettingsScreen(
    setScreen:(String)->Unit,
    currentScreen: String,
    logOut: ()->Unit,
){
    Scaffold(bottomBar = {
        NavBar(
            setScreen = { route->setScreen(route)},
            currentScreen
        )}
    ) {padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text("Settings")
            Button(onClick = logOut) {
                Text("Log Out")
            }
        }
    }
}