package com.example.pokazimi.data.remote.dto

@kotlinx.serialization.Serializable
data class LikeResponse (
    val id: Long,
    val likersId: Long
)