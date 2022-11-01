package com.example.pokazimi.data.remote.dto

@kotlinx.serialization.Serializable
data class PostRequest(
    val image: ByteArray,
    val userId: Int,
    val description: String
)
