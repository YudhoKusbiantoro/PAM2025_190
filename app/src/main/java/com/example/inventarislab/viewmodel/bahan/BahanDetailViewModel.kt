package com.example.inventarislab.viewmodel.bahan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarislab.modeldata.Bahan
import com.example.inventarislab.repositori.RepositoryInventaris
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BahanDetailViewModel(
    private val repository: RepositoryInventaris
) : ViewModel() {

    private val _bahanDetail = MutableStateFlow<Bahan?>(null)
    val bahanDetail: StateFlow<Bahan?> = _bahanDetail

    fun loadBahanById(id: Int) {
        viewModelScope.launch {
            val response = repository.getBahanById(id)
            if (response.status == "success" && response.data != null) {
                _bahanDetail.value = response.data
            }
        }
    }

    fun clearBahanDetail() {
        _bahanDetail.value = null
    }
}