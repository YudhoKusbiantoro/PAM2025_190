package com.example.inventarislab.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class ResponseData<T>(
    val status: String,
    val message: String? = null,
    val data: T? = null
)