package com.example.inventarislab.viewmodel.bahan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarislab.modeldata.Bahan
import com.example.inventarislab.modeldata.ResponseData
import com.example.inventarislab.repositori.RepositoryInventaris
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BahanCreateViewModel(
    private val repository: RepositoryInventaris
) : ViewModel() {

    private val _createResult = MutableStateFlow<ResponseData<Bahan>?>(null)
    val createResult: StateFlow<ResponseData<Bahan>?> = _createResult

    fun createBahan(
        nama: String,
        volume: String,
        expired: String,
        kondisi: String,
        labId: Int
    ) {
        viewModelScope.launch {
            val response = repository.createBahan(
                mapOf(
                    "nama" to nama,
                    "volume" to volume,
                    "expired" to expired,
                    "kondisi" to kondisi,
                    "lab_id" to labId.toString()
                )
            )
            _createResult.value = response
        }
    }

    fun resetCreateResult() {
        _createResult.value = null
    }
}