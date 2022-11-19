package com.example.pokazimi.data.remote.dto

@kotlinx.serialization.Serializable
data class RegistrationRequest(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val password: String
)
