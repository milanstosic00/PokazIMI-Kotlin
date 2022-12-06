package com.example.pokazimi.data.remote.model

@kotlinx.serialization.Serializable
data class Like(
    val id: Long,
    val time: String = "",
    val likersId: Long
)
