package com.example.inventarislab.viewmodel.bahan

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

class BahanListViewModel(
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

    fun loadBahanByLabId(labId: Int) {
        viewModelScope.launch {
            val response = repository.getBahanByLabId(labId)
            if (response.status == "success" && response.data != null) {
                _bahan.value = response.data
                calculateNotification(response.data)
            }
        }
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