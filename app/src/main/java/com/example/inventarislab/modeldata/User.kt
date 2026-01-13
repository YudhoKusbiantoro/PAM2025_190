// modeldata/User.kt
package com.example.inventarislab.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val nama: String,
    val username: String,
    val role: String,
    val lab_id: Int,
    val institusi: String,
    val nama_lab: String
)