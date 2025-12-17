package com.example.financetrackerv2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackerv2.DataModels.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


data class DbState(
    val loading: Boolean = false,
    val success: User? = null,
    val error: String? = null
)

sealed interface DbResult<T>{
    data class Success<T>(val result: T): DbResult<T>
    data class Error<T>(val message:String): DbResult<T>
}

class DbViewModel : ViewModel(){
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    var dbState by mutableStateOf(DbState())
        private set

    fun loadUser() {
        viewModelScope.launch {
            dbState = dbState.copy(loading = true, error = null)

            dbState = when (val result = loadUserAsync(auth.currentUser!!.uid)) {
                is DbResult.Success ->
                    dbState.copy(loading = false, success = result.result)

                is DbResult.Error ->
                    dbState.copy(
                        loading = false,
                        success = null,
                        error = result.message
                    )
            }
        }
    }

    suspend fun loadUserAsync(uid: String): DbResult<User> {
        return try {
            val doc = db
                .collection("users")
                .document(uid)
                .get()
                .await()

            val user = doc.toObject(User::class.java)
                ?: return DbResult.Error("User not found")

            DbResult.Success(user)
        } catch (e: Exception) {
            DbResult.Error(e.message ?: "Unknown error")
        }
    }

    fun createUser(user: User) {
        viewModelScope.launch {
            dbState = dbState.copy(loading = true, error = null)

            dbState = when (val result = createUserAsync(auth.currentUser!!.uid, user)) {
                is DbResult.Success ->
                    dbState.copy(loading = false, success = result.result)

                is DbResult.Error ->
                    dbState.copy(
                        loading = false,
                        success = null,
                        error = result.message
                    )
            }
        }
    }

    suspend fun createUserAsync(uid: String, user : User): DbResult<User> {
        return try {
                db.collection("users")
                .document(uid)
                .set(user)
                .await()

            DbResult.Success(user)
        } catch (e: Exception) {
            DbResult.Error(e.message ?: "Unknown error")
        }
    }

}


