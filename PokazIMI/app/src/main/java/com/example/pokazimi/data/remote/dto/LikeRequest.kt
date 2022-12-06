package com.example.pokazimi.data.remote.dto

@kotlinx.serialization.Serializable
data class LikeRequest (
    val post: Long,
    val likersId: Long
)