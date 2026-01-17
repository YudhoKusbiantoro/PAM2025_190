package com.example.inventarislab.viewmodel.bahan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarislab.modeldata.ResponseData
import com.example.inventarislab.repositori.RepositoryInventaris
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BahanDeleteViewModel(
    private val repository: RepositoryInventaris
) : ViewModel() {

    private val _deleteResult = MutableStateFlow<String?>(null)
    val deleteResult: StateFlow<String?> = _deleteResult

    fun deleteBahan(id: Int, labId: Int) {
        viewModelScope.launch {
            val response = repository.deleteBahan(
                mapOf(
                    "id" to id.toString(),
                    "lab_id" to labId.toString()
                )
            )
            if (response.status == "success") {
                _deleteResult.value = response.message
            }
        }
    }

    fun resetDeleteResult() {
        _deleteResult.value = null
    }
}