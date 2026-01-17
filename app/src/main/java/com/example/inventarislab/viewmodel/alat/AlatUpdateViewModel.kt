package com.example.inventarislab.viewmodel.alat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarislab.modeldata.Alat
import com.example.inventarislab.modeldata.ResponseData
import com.example.inventarislab.repositori.RepositoryInventaris
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlatUpdateViewModel(
    private val repository: RepositoryInventaris
) : ViewModel() {

    private val _updateResult = MutableStateFlow<ResponseData<Alat>?>(null)
    val updateResult: StateFlow<ResponseData<Alat>?> = _updateResult

    fun updateAlat(
        id: Int,
        nama: String,
        jumlah: String,
        terakhirKalibrasi: String,
        intervalKalibrasi: String,
        satuanInterval: String,
        kondisi: String,
        labId: Int
    ) {
        viewModelScope.launch {
            val response = repository.updateAlat(
                mapOf(
                    "id" to id.toString(),
                    "nama" to nama,
                    "jumlah" to jumlah,
                    "terakhir_kalibrasi" to terakhirKalibrasi,
                    "interval_kalibrasi" to intervalKalibrasi,
                    "satuan_interval" to satuanInterval,
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