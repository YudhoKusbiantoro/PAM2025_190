// viewmodel/RegisterViewModel.kt
package com.example.inventarislab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarislab.apiservice.ApiClient
import com.example.inventarislab.modeldata.Lab
import com.example.inventarislab.modeldata.ResponseData
import com.example.inventarislab.modeldata.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _registerResult = MutableStateFlow<ResponseData<User>?>(null)
    val registerResult: StateFlow<ResponseData<User>?> = _registerResult

    private val _labs = MutableStateFlow<List<Lab>>(emptyList())
    val labs: StateFlow<List<Lab>> = _labs

    fun register(
        nama: String,
        username: String,
        password: String,
        institusi: String? = null,
        namaLab: String? = null,
        labId: Int? = null
    ) {
        viewModelScope.launch {
            try {
                val requestBody = mutableMapOf<String, String>(
                    "nama" to nama,
                    "username" to username,
                    "password" to password
                )

                if (institusi != null && namaLab != null) {
                    requestBody["institusi"] = institusi
                    requestBody["nama_lab"] = namaLab
                } else if (labId != null) {
                    requestBody["lab_id"] = labId.toString()
                }

                val response = ApiClient.apiService.register(requestBody)
                _registerResult.value = response
            } catch (e: Exception) {
                _registerResult.value = ResponseData(
                    status = "error",
                    message = "Gagal terhubung ke server."
                )
            }
        }
    }

    fun loadLabs() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getLabs()
                if (response.status == "success" && response.data != null) {
                    _labs.value = response.data
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ✅ Tambahkan fungsi reset
    fun resetRegisterResult() {
        _registerResult.value = null
    }
}