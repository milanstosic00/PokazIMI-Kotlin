package com.example.pokazimi.data.remote.dto

@kotlinx.serialization.Serializable
data class Like(
    val post: Int,
    val likersId: Long,
    val time: String
)
