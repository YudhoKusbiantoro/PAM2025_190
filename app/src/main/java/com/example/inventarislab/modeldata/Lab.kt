package com.example.inventarislab.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class Lab(
    val id: Int,
    val institusi: String,
    val nama_lab: String
)