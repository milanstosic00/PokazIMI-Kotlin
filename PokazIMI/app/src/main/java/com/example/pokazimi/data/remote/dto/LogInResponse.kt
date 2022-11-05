package com.example.pokazimi.data.remote.dto


@kotlinx.serialization.Serializable
data class LogInResponse(
    val accessToken: String,
    val refreshToken: String
)
