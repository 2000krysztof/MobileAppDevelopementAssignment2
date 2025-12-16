package com.example.financetrackerv2

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



data class LoginUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
sealed interface AuthResult {
    data class Success(val user: FirebaseUser) : AuthResult
    data class Error(val message: String) : AuthResult
}

class LoginViewModel : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, error = null)

            uiState = when (val result = loginAsync(email, password)) {
                is AuthResult.Success ->
                    uiState.copy(loading = false, success = true)

                is AuthResult.Error ->
                    uiState.copy(
                        loading = false,
                        error = result.message
                    )
            }
        }
    }

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, error = null)

            uiState = when (val result = signUpAsync(email, password)) {
                is AuthResult.Success ->
                    uiState.copy(loading = false, success = true)

                is AuthResult.Error ->
                    uiState.copy(
                        loading = false,
                        error = result.message
                    )
            }
        }
    }

    suspend fun loginAsync(email: String, password: String): AuthResult {
        return try {
            val result = FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .await()

            AuthResult.Success(result.user!!)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun signUpAsync(email: String, password: String): AuthResult{
        return try {
            val result = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .await()
            AuthResult.Success(result.user!!)
        }catch (e: Exception){
            AuthResult.Error(e.message ?: "Unknown error")
        }
    }
}


