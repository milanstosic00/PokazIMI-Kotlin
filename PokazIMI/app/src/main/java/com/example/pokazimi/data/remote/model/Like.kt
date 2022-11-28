package com.example.pokazimi.data.remote.model

@kotlinx.serialization.Serializable
data class Like(
    val post: Int,
    val likersId: Long,
    val time: String
)
