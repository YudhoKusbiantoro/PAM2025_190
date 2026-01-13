package com.example.inventarislab.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class Alat(
    val id: Int,
    val nama: String,
    val jumlah: Int,
    val terakhir_kalibrasi: String?,
    val interval_kalibrasi: Int,
    val satuan_interval: String,
    val kondisi: String,
    val lab_id: Int,
    val institusi: String? = null,
    val nama_lab: String? = null
)