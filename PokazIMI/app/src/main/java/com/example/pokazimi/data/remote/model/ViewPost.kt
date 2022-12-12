package com.example.pokazimi.data.remote.model

import com.example.pokazimi.data.remote.dto.LikeResponse

@kotlinx.serialization.Serializable
data class ViewPost (
    val id: Long,
    val user: PostUser,
    val time: String,
    val image0: String?,
    val image1: String?,
    val image2: String?,
    val image3: String?,
    val image4: String?,
    val description: String,
    val lat: Double,
    val lon: Double,
    val likes: Array<Like>?,
    val comments: Array<Comment>?,
    val likedByUser: Boolean,
    val likesNum: Int?,
    val commentsNum: Int?
)
