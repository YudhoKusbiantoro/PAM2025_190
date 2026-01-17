package com.example.inventarislab.viewmodel.alat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarislab.modeldata.Alat
import com.example.inventarislab.repositori.RepositoryInventaris
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlatDetailViewModel(
    private val repository: RepositoryInventaris
) : ViewModel() {

    private val _alatDetail = MutableStateFlow<Alat?>(null)
    val alatDetail: StateFlow<Alat?> = _alatDetail

    fun loadAlatById(id: Int) {
        viewModelScope.launch {
            val response = repository.getAlatById(id)
            if (response.status == "success" && response.data != null) {
                _alatDetail.value = response.data
            }
        }
    }

    fun clearAlatDetail() {
        _alatDetail.value = null
    }
}