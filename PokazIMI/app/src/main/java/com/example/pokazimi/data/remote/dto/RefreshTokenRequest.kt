package com.example.pokazimi.data.remote.dto


@kotlinx.serialization.Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)
