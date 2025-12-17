package com.example.financetrackerv2

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackerv2.DataModels.BudgetEntry
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

data class DbEntriesState(
    val loading: Boolean = false,
    var success: List<BudgetEntryUi>? = null,
    val error: String? = null
)

data class DbEntryAddState(
    val loading: Boolean = false,
    val success: BudgetEntryUi? = null,
    val error: String? = null
)

data class BudgetEntryUi(
    val id: String,
    val entry: BudgetEntry
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
    var entriesState by mutableStateOf(DbEntriesState())
        private set

    var entryAddedState by mutableStateOf(DbEntryAddState())
        private set
    fun loadUser() {
        viewModelScope.launch {
            dbState = dbState.copy(loading = true, error = null)


            dbState = when (val result = loadUserAsync(auth.currentUser!!.uid)) {
                is DbResult.Success -> {
                    loadEntries()
                    dbState.copy(loading = false, success = result.result)
                }


                is DbResult.Error ->
                    dbState.copy(
                        loading = false,
                        success = null,
                        error = result.message
                    )
            }

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

    fun loadEntries() {
        viewModelScope.launch {
            entriesState = entriesState.copy(loading = true, error = null)

            entriesState = when (val result = getAllBudgetEntriesAsync()) {
                is DbResult.Success ->
                    entriesState.copy(loading = false, success = result.result)
                is DbResult.Error ->
                    entriesState.copy(
                        loading = false,
                        success = null,
                        error = result.message
                    )
            }
        }
    }

    fun addEntry(entry: BudgetEntry) {
        viewModelScope.launch {
            entryAddedState = entryAddedState.copy(loading = true, error = null)

            entryAddedState = when (val result = addBudgetEntryAsync(entry)) {
                is DbResult.Success ->
                    entryAddedState.copy(loading = false, success = result.result)
                is DbResult.Error ->
                    entryAddedState.copy(
                        loading = false,
                        success = null,
                        error = result.message
                    )
            }
        }
    }

    fun updateEntry(entry: BudgetEntryUi) {
        viewModelScope.launch {
            entryAddedState = entryAddedState.copy(loading = true, error = null)

            entryAddedState = when (val result = updateEntryAsync(entry)) {
                is DbResult.Success ->
                    entryAddedState.copy(loading = false, success = result.result)
                is DbResult.Error ->
                    entryAddedState.copy(
                        loading = false,
                        success = null,
                        error = result.message
                    )
            }
        }
    }

    fun deleteEntry(entry: BudgetEntryUi) {
        viewModelScope.launch {
            entryAddedState = entryAddedState.copy(loading = true, error = null)

            entryAddedState = when (val result = deleteEntryAsync(entry)) {
                is DbResult.Success ->
                    entryAddedState.copy(loading = false, success = result.result)
                is DbResult.Error ->
                    entryAddedState.copy(
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


    suspend fun getAllBudgetEntriesAsync(): DbResult<List<BudgetEntryUi>> {
        val uid = auth.currentUser?.uid ?: return DbResult.Error("Not Authenticated")

        return try {
            val snapshot = db.collection("users")
                .document(uid)
                .collection("budgetEntries")
                .orderBy("timestamp")
                .get()
                .await()

            val uiEntries = snapshot.documents.mapNotNull { doc ->
                val entry = doc.toObject(BudgetEntry::class.java)
                entry?.let {
                    BudgetEntryUi(
                        id = doc.id,
                        entry = it
                    )
                }
            }

            DbResult.Success(uiEntries)
        } catch (e: Exception) {
            DbResult.Error(e.message ?: "Unknown error")
        }
    }



    suspend fun addBudgetEntryAsync(budgetEntry: BudgetEntry): DbResult<BudgetEntryUi> {
        val uid = auth.currentUser?.uid
            ?: return DbResult.Error("Not Authenticated")

        return try {
            val ref = db.collection("users")
                .document(uid)
                .collection("budgetEntries")
                .add(budgetEntry)
                .await()
            val budgetEntryUi = BudgetEntryUi(ref.id, budgetEntry)
            entriesState = entriesState.copy(
                success = (entriesState.success ?: emptyList()) + budgetEntryUi
            )
            DbResult.Success(budgetEntryUi)

        } catch (e: Exception) {
            DbResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun updateEntryAsync(entryUi: BudgetEntryUi): DbResult<BudgetEntryUi> {
        val uid = auth.currentUser?.uid
            ?: return DbResult.Error("Not Authenticated")

        return try {
            db.collection("users")
                .document(uid)
                .collection("budgetEntries")
                .document(entryUi.id)
                .set(entryUi.entry)
                .await()

            val updated = BudgetEntryUi(
                id = entryUi.id,
                entry = entryUi.entry.copy()
            )

            entriesState = entriesState.copy(
                success = entriesState.success?.map {
                    if (it.id == updated.id) updated else it
                }
            )
            DbResult.Success(entryUi)

        } catch (e: Exception) {
            DbResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun deleteEntryAsync(entryUi: BudgetEntryUi): DbResult<BudgetEntryUi> {
        val uid = auth.currentUser?.uid
            ?: return DbResult.Error("Not Authenticated")

        return try {
            val ref = db.collection("users")
                .document(uid)
                .collection("budgetEntries")
                .document(entryUi.id)
                .delete()
                .await()

            entriesState = entriesState.copy(
                success = entriesState.success
                    ?.filterNot { it.id == entryUi.id }
            )
            DbResult.Success(entryUi)

        } catch (e: Exception) {
            DbResult.Error(e.message ?: "Unknown error")
        }
    }

}


