package com.example.inventarislab.viewmodel.alat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarislab.modeldata.Alat
import com.example.inventarislab.repositori.RepositoryInventaris
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AlatListViewModel(
    private val repository: RepositoryInventaris
) : ViewModel() {

    private val _alat = MutableStateFlow<List<Alat>>(emptyList())
    val alat: StateFlow<List<Alat>> = _alat

    private val _notification = MutableStateFlow<Notification?>(null)
    val notification: StateFlow<Notification?> = _notification

    data class Notification(
        val total: Int,
        val expired: Int,
        val rusak: Int
    )

    fun loadAlatByLabId(labId: Int) {
        viewModelScope.launch {
            val response = repository.getAlatByLabId(labId)
            if (response.status == "success" && response.data != null) {
                _alat.value = response.data
                calculateNotification(response.data)
            }
        }
    }

    private fun calculateNotification(alatList: List<Alat>) {
        val today = Calendar.getInstance()
        val total = alatList.size
        val rusak = alatList.count { it.kondisi == "Rusak" }
        val perluKalibrasi = alatList.count { alat ->
            try {
                if (alat.terakhir_kalibrasi.isNullOrEmpty() || alat.interval_kalibrasi <= 0) return@count false
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val kalibrasiDate = formatter.parse(alat.terakhir_kalibrasi) ?: return@count false
                val kalibrasiCal = Calendar.getInstance().apply { time = kalibrasiDate }
                when (alat.satuan_interval.lowercase()) {
                    "hari" -> kalibrasiCal.add(Calendar.DAY_OF_YEAR, alat.interval_kalibrasi)
                    "minggu" -> kalibrasiCal.add(Calendar.WEEK_OF_YEAR, alat.interval_kalibrasi)
                    "bulan" -> kalibrasiCal.add(Calendar.MONTH, alat.interval_kalibrasi)
                    "tahun" -> kalibrasiCal.add(Calendar.YEAR, alat.interval_kalibrasi)
                }
                val diffDays = (kalibrasiCal.timeInMillis - today.timeInMillis) / (24 * 60 * 60 * 1000)
                diffDays <= 0
            } catch (e: Exception) {
                false
            }
        }
        _notification.value = Notification(total, perluKalibrasi, rusak)
    }
}