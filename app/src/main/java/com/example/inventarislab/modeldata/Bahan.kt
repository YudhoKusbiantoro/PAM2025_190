package com.example.inventarislab.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class Bahan(
    val id: Int,
    val nama: String,
    val volume: String,
    val expired: String,
    val kondisi: String,
    val lab_id: Int,
    val institusi: String,
    val nama_lab: String
)