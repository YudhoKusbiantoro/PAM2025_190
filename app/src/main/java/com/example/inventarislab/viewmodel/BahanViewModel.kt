package com.example.inventarislab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarislab.modeldata.Bahan
import com.example.inventarislab.repositori.RepositoryInventaris
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BahanViewModel(
    private val repository: RepositoryInventaris
) : ViewModel() {

    private val _bahan = MutableStateFlow<List<Bahan>>(emptyList())
    val bahan: StateFlow<List<Bahan>> = _bahan

    private val _notification = MutableStateFlow<Notification?>(null)
    val notification: StateFlow<Notification?> = _notification

    data class Notification(
        val total: Int,
        val expired: Int,
        val hampirExpired: Int,
        val rusak: Int
    )

    private val _bahanDetail = MutableStateFlow<Bahan?>(null)
    val bahanDetail: StateFlow<Bahan?> = _bahanDetail

    private val _deleteResult = MutableStateFlow<String?>(null)
    val deleteResult: StateFlow<String?> = _deleteResult

    fun loadBahanByLabId(labId: Int) {
        viewModelScope.launch {
            val response = repository.getBahanByLabId(labId)
            if (response.status == "success" && response.data != null) {
                _bahan.value = response.data
                calculateNotification(response.data)
            }
        }
    }

    fun loadBahanById(id: Int) {
        viewModelScope.launch {
            val response = repository.getBahanById(id)
            if (response.status == "success" && response.data != null) {
                _bahanDetail.value = response.data
            }
        }
    }

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
            if (response.status == "success") {
                loadBahanByLabId(labId)
            }
        }
    }

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
            if (response.status == "success") {
                loadBahanByLabId(labId)
            }
        }
    }

    fun deleteBahan(id: Int, labId: Int) {
        viewModelScope.launch {
            val response = repository.deleteBahan(
                mapOf(
                    "id" to id.toString(),
                    "lab_id" to labId.toString()
                )
            )
            if (response.status == "success") {
                _bahanDetail.value = null
                _deleteResult.value = response.message
                loadBahanByLabId(labId)
            }
        }
    }

    fun resetDeleteResult() {
        _deleteResult.value = null
    }

    fun clearBahanDetail() {
        _bahanDetail.value = null
    }

    private fun calculateNotification(bahanList: List<Bahan>) {
        val today = Calendar.getInstance()
        val total = bahanList.size
        val expired = bahanList.count { it.kondisi == "Expired" }
        val rusak = bahanList.count { it.kondisi == "Rusak" }
        val hampirExpired = bahanList.count { bahan ->
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val expiredDate = formatter.parse(bahan.expired) ?: return@count false
                val diffDays = ((expiredDate.time - today.timeInMillis) / (24 * 60 * 60 * 1000)).toInt()
                diffDays in 1..7
            } catch (e: Exception) {
                false
            }
        }
        _notification.value = Notification(total, expired, hampirExpired, rusak)
    }
}