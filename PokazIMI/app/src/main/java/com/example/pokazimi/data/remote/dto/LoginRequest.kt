package com.example.pokazimi.data.remote.dto

@kotlinx.serialization.Serializable
data class LoginRequest(
    val email: String,
    val password: String
)
