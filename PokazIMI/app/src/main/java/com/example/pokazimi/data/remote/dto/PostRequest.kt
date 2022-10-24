package com.example.pokazimi.data.remote.dto

@kotlinx.serialization.Serializable
data class PostRequest(
    val body: String,
    val title: String,
    val userId: Int
)
