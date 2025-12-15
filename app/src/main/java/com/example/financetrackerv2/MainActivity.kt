package com.example.financetrackerv2

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Label
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import com.example.financetrackerv2.ui.theme.FinanceTrackerTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.initialize(this)
        enableEdgeToEdge()
        setContent {
            FinanceTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var mode by remember { mutableStateOf(LoginState.LOG_IN) }
                    Column(Modifier.padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){
                        Greeting(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 32.dp))

                        when(mode){
                            LoginState.LOG_IN -> LoginForm(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                onLogin = { username, password ->login(username, password)})
                            LoginState.SIGN_UP -> SignUpForm(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                onSignup = { username, password ->signUp(username, password)})
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
        }
    }
}
enum class LoginState {
    LOG_IN,
    SIGN_UP
}
fun login(username: String, password:String): Unit{
    Log.d("loginScreen", "Login")
}

fun signUp(username: String, password: String): Unit{
    Log.d("loginScreen", "Signup")
}
@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Text(
        text = "Finance Tracker",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FinanceTrackerTheme {
        Greeting()
    }
}

@Composable
fun LoginForm(modifier: Modifier = Modifier,
              onLogin: (String, String)-> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        TextField(
            modifier = modifier,
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        TextField(
            modifier = modifier,
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )
        Button(
            onClick = {onLogin(username,password )},
            modifier = modifier,
        ){
            Text("Login")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun LoginFormPreview() {
    FinanceTrackerTheme {
        LoginForm(
            onLogin = { username, password ->login(username, password)})
    }
}

@Composable
fun SignUpForm(modifier: Modifier = Modifier,
               onSignup: (String, String)-> Unit) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    Column(modifier = modifier) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
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
                onSignup(username,password )},
            modifier = modifier,
        ){
            Text("Sign Up")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SignupFormPreview() {
    FinanceTrackerTheme {
        SignUpForm(
            onSignup = { username, password ->login(username, password)})
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

