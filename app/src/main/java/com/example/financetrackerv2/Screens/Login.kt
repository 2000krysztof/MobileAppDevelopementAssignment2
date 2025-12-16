package com.example.financetrackerv2.Screens

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.financetrackerv2.LoginUiState
import com.example.financetrackerv2.ui.theme.FinanceTrackerTheme
import com.google.firebase.auth.FirebaseAuth

enum class LoginState {
    LOG_IN,
    SIGN_UP
}

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    onSignup: (String, String) -> Unit,
    uiState: LoginUiState
){
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        var mode by remember { mutableStateOf(LoginState.LOG_IN) }
        Column(Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Greeting(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 32.dp))

            when(mode){
                LoginState.LOG_IN -> LoginForm(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onLogin = { email, password ->onLogin(email,password)})
                LoginState.SIGN_UP -> SignUpForm(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onSignup = { email, password ->onSignup(email, password)})
            }

            ToggleLoginSignup(mode = mode, toggle = {
                mode = when(mode){
                    LoginState.LOG_IN -> LoginState.SIGN_UP
                    LoginState.SIGN_UP -> LoginState.LOG_IN
                }
            })
        }


    }
}




@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Text(
        text = "Finance Tracker",
        modifier = modifier
    )
}

@Composable
fun LoginForm(modifier: Modifier = Modifier,
              onLogin: (String, String)-> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        TextField(
            modifier = modifier,
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        TextField(
            modifier = modifier,
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )
        Button(
            onClick = {onLogin(email,password )},
            modifier = modifier,
        ){
            Text("Login")
        }
    }

}

@Composable
fun SignUpForm(modifier: Modifier = Modifier,
               onSignup: (String, String)-> Unit) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    Column(modifier = modifier) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("email") }
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )
        TextField(
            value = repeatPassword,
            onValueChange = { repeatPassword = it },
            label = { Text("Repeat Password") }
        )
        if(isError){
            Text(errorMessage, color= Color(1,0,0))
        }
        Button(
            onClick = {
                if(repeatPassword != password){
                    isError = true
                    errorMessage = "Repeated Password does not match"
                    return@Button
                }
                onSignup(email,password )},
            modifier = modifier,
        ){
            Text("Sign Up")
        }
    }

}


@Composable
fun ToggleLoginSignup(modifier: Modifier = Modifier, mode: LoginState, toggle: ()-> Unit ){
    val text = when (mode){
        LoginState.LOG_IN -> "Sign Up"
        LoginState.SIGN_UP -> "Log In"
    }
    Button(
        onClick = toggle,
        modifier = modifier){
        Text(text)
    }
}