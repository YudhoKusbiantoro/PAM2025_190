// viewmodel/RegisterViewModel.kt
package com.example.inventarislab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarislab.modeldata.Lab
import com.example.inventarislab.modeldata.ResponseData
import com.example.inventarislab.modeldata.User
import com.example.inventarislab.repositori.RepositoryInventaris
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: RepositoryInventaris
) : ViewModel() {

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

            // ✅ LANGSUNG ASSIGN RESPON — TIDAK PERLU TRY-CATCH
            val response = repository.register(requestBody)
            _registerResult.value = response
        }
    }

    fun loadLabs() {
        viewModelScope.launch {
            val response = repository.getLabs()
            _labs.value = response.data ?: emptyList()
        }
    }

    fun resetRegisterResult() {
        _registerResult.value = null
    }
}