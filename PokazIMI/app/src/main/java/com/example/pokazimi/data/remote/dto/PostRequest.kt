package com.example.pokazimi.data.remote.dto

@kotlinx.serialization.Serializable
data class PostRequest(
    val description: String,
    val image: ByteArray
)
