package com.example.financetrackerv2.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.financetrackerv2.Screens.Screen

@Composable
fun NavBar(
    setScreen:(String)->Unit,
    currentRoute: Screen
) {

    NavigationBar {
        NavigationBar {
            bottomNavItems.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.screen,
                    onClick = {
                        setScreen(item.screen.route)
                    },
                    icon = { Icon(item.icon, contentDescription = item.label) }
                )
            }
        }

    }
}
data class NavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf<NavItem>(
    NavItem(Screen.Home, Icons.Default.Home, "Home"),
    NavItem(Screen.Home, Icons.Default.Menu, "Budget List"),
    NavItem(Screen.Home, Icons.Default.Settings, "Settings"),
)