package com.example.pokazimi.data.remote.model

@kotlinx.serialization.Serializable
data class Like(
    val post: Long,
    val likersId: Long
)
