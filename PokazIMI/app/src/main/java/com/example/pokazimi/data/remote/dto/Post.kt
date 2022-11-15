package com.example.pokazimi.data.remote.dto

import java.time.LocalDateTime

@kotlinx.serialization.Serializable
data class Post (
    val id: Int,
    val time: String,
    val image: String?,
    val description: String,
    val likes: Array<Int>,
    val comments: Array<Int>
)