package com.example.inventarislab.viewmodel.bahan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarislab.modeldata.Bahan
import com.example.inventarislab.modeldata.ResponseData
import com.example.inventarislab.repositori.RepositoryInventaris
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BahanUpdateViewModel(
    private val repository: RepositoryInventaris
) : ViewModel() {

    private val _updateResult = MutableStateFlow<ResponseData<Bahan>?>(null)
    val updateResult: StateFlow<ResponseData<Bahan>?> = _updateResult

    fun updateBahan(
        id: Int,
        nama: String,
        volume: String,
        expired: String,
        kondisi: String,
        labId: Int
    ) {
        viewModelScope.launch {
            val response = repository.updateBahan(
                mapOf(
                    "id" to id.toString(),
                    "nama" to nama,
                    "volume" to volume,
                    "expired" to expired,
                    "kondisi" to kondisi,
                    "lab_id" to labId.toString()
                )
            )
            _updateResult.value = response
        }
    }

    fun resetUpdateResult() {
        _updateResult.value = null
    }
}