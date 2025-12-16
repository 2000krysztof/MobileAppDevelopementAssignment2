package com.example.financetrackerv2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.financetrackerv2.ui.theme.FinanceTrackerTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.financetrackerv2.Screens.LoginScreen
import com.example.financetrackerv2.Screens.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.initialize(this)

        enableEdgeToEdge()
        setContent {
            FinanceTrackerTheme {
                AppNavGraph()
            }
        }
    }
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val loginViewModel = LoginViewModel()
    var startDestination = Screen.Login.route
    if(auth.currentUser != null){
        startDestination = Screen.Home.route
    }

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
                    }
                },
                loginViewModel.uiState
            )
        }
        composable(Screen.Home.route){
            Text("Home")
        }
    }
}


