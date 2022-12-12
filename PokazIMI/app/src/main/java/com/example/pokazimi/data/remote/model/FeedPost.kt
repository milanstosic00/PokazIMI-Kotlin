package com.example.pokazimi.data.remote.model

import com.example.pokazimi.data.remote.dto.LikeResponse

@kotlinx.serialization.Serializable
data class FeedPost (
    val id: Long,
    val user: PostUser,
    val time: String,
    val image0: String?,
    val description: String,
    val lat: Double,
    val lon: Double,
    val likes: Array<Like>?,
    val comments: Array<Comment>?,
    val likedByUser: Boolean,
    val likesNum: Int?,
    val commentsNum: Int?
)