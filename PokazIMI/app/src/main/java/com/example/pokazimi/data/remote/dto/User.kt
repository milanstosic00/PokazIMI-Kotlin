package com.example.pokazimi.data.remote.dto
@kotlinx.serialization.Serializable
data class User (
    val firstName: String,
    val lastname: String,
    val username: String,
    val email: String
)