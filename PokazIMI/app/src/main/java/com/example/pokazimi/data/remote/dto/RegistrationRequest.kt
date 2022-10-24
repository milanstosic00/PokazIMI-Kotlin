package com.example.pokazimi.data.remote.dto

@kotlinx.serialization.Serializable
data class RegistrationRequest(
    val firstName: String,
    val lastname: String,
    val username: String,
    val email: String,
    val password: String
)
