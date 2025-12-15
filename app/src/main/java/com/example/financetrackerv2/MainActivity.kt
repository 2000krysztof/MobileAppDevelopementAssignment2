package com.example.financetrackerv2

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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.financetrackerv2.ui.theme.FinanceTrackerTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.initialize(this)
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            Log.d("user",(auth.currentUser!!.email!!))
            //TODO put logic here to move to another screen
        }
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
                                onLogin = { email, password ->login(this@MainActivity,email, password)})
                            LoginState.SIGN_UP -> SignUpForm(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                onSignup = { email, password ->signUp(this@MainActivity,email, password)})
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
fun login(context:ComponentActivity,email: String, password:String): Unit{
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(context) { task ->
            if (task.isSuccessful) {
                Log.d("Login", "signInWithEmailAndPassword:success")
                val user = auth.currentUser
            } else {
                Log.w("Login", "signInWithEmailAndPassword:failure", task.exception)
            }
        }
}

fun signUp(context:ComponentActivity, email: String, password: String): Unit{
    val auth = FirebaseAuth.getInstance()
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(context) { task ->
            if (task.isSuccessful) {
                Log.d("Login", "createUserWithEmailAndPassword:success")
                val user = auth.currentUser
            } else {
                Log.w("Login", "createUserWithEmailAndPassword:failure", task.exception)
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

