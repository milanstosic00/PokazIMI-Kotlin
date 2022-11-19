package com.example.pokazimi.data.remote.dto

@kotlinx.serialization.Serializable
data class PostRequest(
    val image: ByteArray,
    val user: Long,
    val description: String,
    val lat: Double,
    val lon: Double
)
