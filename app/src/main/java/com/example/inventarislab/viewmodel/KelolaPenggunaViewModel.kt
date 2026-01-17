package com.example.inventarislab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarislab.apiservice.ApiClient
import com.example.inventarislab.modeldata.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class KelolaPenggunaViewModel : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _updateResult = MutableStateFlow<String?>(null)
    val updateResult: StateFlow<String?> = _updateResult

    private val _deleteResult = MutableStateFlow<String?>(null)
    val deleteResult: StateFlow<String?> = _deleteResult

    fun loadUsersByLabId(labId: Int) {
        viewModelScope.launch {
            try {
                // Konversi labId ke String
                val response = ApiClient.apiService.getUsersByLabId(mapOf("lab_id" to labId.toString()))
                if (response.status == "success" && response.data != null) {
                    _users.value = response.data
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateUser(id: Int, nama: String, username: String, role: String, password: String?) {
        viewModelScope.launch {
            try {
                val requestBody = mutableMapOf<String, String>()
                requestBody["id"] = id.toString()
                requestBody["nama"] = nama
                requestBody["username"] = username
                requestBody["role"] = role
                if (!password.isNullOrBlank()) {
                    requestBody["password"] = password
                }

                val response = ApiClient.apiService.updateUser(requestBody)

                if (response.status == "success") {
                    val message = "User berhasil diperbarui"
                    println("✅ UPDATE RESULT: $message") // ✅ LOG DI SINI
                    _updateResult.value = message
                } else {
                    val message = response.message ?: "Gagal memperbarui user"
                    println("❌ UPDATE RESULT: $message") // ✅ LOG JUGA DI SINI
                    _updateResult.value = message
                }
            } catch (e: Exception) {
                val message = "Gagal memperbarui user: ${e.message}"
                println("💥 UPDATE ERROR: $message") // ✅ LOG ERROR
                e.printStackTrace()
                _updateResult.value = message
            }
        }
    }

    fun deleteUser(id: Int, labId: Int) {
        viewModelScope.launch {
            try {
                // ✅ Konversi ke String
                val response = ApiClient.apiService.deleteUser(
                    mapOf("id" to id.toString(), "lab_id" to labId.toString())
                )
                _deleteResult.value = if (response.status == "success") "User berhasil dihapus" else response.message
            } catch (e: Exception) {
                e.printStackTrace()
                _deleteResult.value = "Gagal menghapus user"
            }
        }
    }

    fun resetUpdateResult() {
        _updateResult.value = null
    }

    fun resetDeleteResult() {
        _deleteResult.value = null
    }
}