package com.example.pokazimi.data.remote.dto

@kotlinx.serialization.Serializable
data class CommentRequest(
    val post_id: Long,
    val commentersId: Long,
    val content: String
)
