package com.example.pokazimi.data.remote.dto

@kotlinx.serialization.Serializable
data class LoginRequest(
    val username: String,
    val password: String
)
