package com.example.financetrackerv2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.financetrackerv2.DataModels.User
import com.example.financetrackerv2.Screens.BudgetListScreen
import com.example.financetrackerv2.Screens.HomeScreen
import com.example.financetrackerv2.Screens.LoginScreen
import com.example.financetrackerv2.Screens.Screen
import com.example.financetrackerv2.Screens.SettingsScreen
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material3.CircularProgressIndicator
import com.example.financetrackerv2.DataModels.BudgetEntry


@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val loginViewModel = LoginViewModel()
    val dbViewModel = DbViewModel()

    val startDestination = Screen.Login.route

    var currentScreen by remember { mutableStateOf(startDestination)}

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLogin = { email,password ->
                    loginViewModel.login(email, password)
                    if(loginViewModel.uiState.success){
                        navController.navigate(Screen.Home.route)
                    }
                },
                onSignup = { email, password ->
                    loginViewModel.signup(email,password)
                    if(loginViewModel.uiState.success){
                        navController.navigate(Screen.Home.route)
                        currentScreen = Screen.Home.route
                        dbViewModel.createUser(User(email))
                    }
                },
                loginViewModel.uiState


            )
            LaunchedEffect(loginViewModel.uiState.success) {
                if (loginViewModel.uiState.success) {
                    if(loginViewModel.uiState.isNewUser) {
                        dbViewModel.createUser(User(loginViewModel.uiState.email))
                    }
                    navController.navigate(Screen.Home.route)
                    currentScreen = Screen.Home.route
                    dbViewModel.loadUser()
                }
            }

        }



        composable(Screen.Home.route){
            if(dbViewModel.entriesState.loading){
                CircularProgressIndicator()
            }else{
                HomeScreen(
                    setScreen = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        currentScreen = route
                    },
                    currentScreen =  currentScreen,
                    addBudgetEntry = { title, description, timestamp, amount ->
                        val budgetEntry = BudgetEntry(title,description,timestamp,amount)
                        dbViewModel.addEntry(budgetEntry)
                    },
                    deleteBudgetEntry = { entry->
                      dbViewModel.deleteEntry(entry)
                    },
                    editBudgetEntry = { entry->
                        dbViewModel.updateEntry(entry)
                    },
                    dbViewModel.entriesState.success ?: emptyList()
                )
            }


        }

        composable(Screen.Settings.route){
            SettingsScreen(
                setScreen = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                    currentScreen = route
                },
                currentScreen =  currentScreen,
                logOut = {
                    loginViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                    currentScreen = Screen.Login.route
                }
            )
        }

        composable(Screen.BudgetList.route){
            BudgetListScreen(
                setScreen = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                    currentScreen = route
                },
                currentScreen =  currentScreen,
                addBudgetEntry = { title, description, timestamp, amount ->
                    val budgetEntry = BudgetEntry(title,description,timestamp,amount)
                    dbViewModel.addEntry(budgetEntry)
                },
                deleteBudgetEntry = { entry->
                    dbViewModel.deleteEntry(entry)
                },
                editBudgetEntry = { entry->
                    dbViewModel.updateEntry(entry)
                },
                dbViewModel.entriesState.success ?: emptyList()
            )
        }



    }
}