package com.example.pokazimi.data.remote.model

import com.example.pokazimi.data.remote.dto.LikeResponse

@kotlinx.serialization.Serializable
data class ViewPost (
    val id: Long,
    val user: PostUser,
    val time: String,
    val image: String?,
    val description: String,
    val lat: Double,
    val lon: Double,
    val likes: Array<LikeResponse>?,
    val comments: Array<Comment>?,
    val likedByUser: Boolean
)
