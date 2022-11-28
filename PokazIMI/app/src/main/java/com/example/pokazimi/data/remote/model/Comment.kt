package com.example.pokazimi.data.remote.model

@kotlinx.serialization.Serializable
data class Comment(
    val id: Long,
    val postId: Long,
    val content: String,
    val commentersId: Long,
    val time: String
)
