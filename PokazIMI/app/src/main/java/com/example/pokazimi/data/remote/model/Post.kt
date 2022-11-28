package com.example.pokazimi.data.remote.model

import com.example.pokazimi.data.remote.model.Comment
import com.example.pokazimi.data.remote.model.Like

@kotlinx.serialization.Serializable
data class Post (
    val id: Long,
    val time: String,
    val image: String?,
    val description: String,
    val lat: Double,
    val lon: Double,
    val likes: Array<Like>?,
    val comments: Array<Comment>?,
)