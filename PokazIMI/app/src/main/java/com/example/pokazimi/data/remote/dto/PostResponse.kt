package com.example.pokazimi.data.remote.dto

@kotlinx.serialization.Serializable
data class PostResponse(
    val body: String,
    val title: String,
    val id: Int,
    val userId: Int
)
