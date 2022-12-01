package com.example.pokazimi.data.remote.model

@kotlinx.serialization.Serializable
data class ViewPost (
    val id: Long,
    val user: PostUser,
    val time: String,
    val image: String?,
    val description: String,
    val lat: Double,
    val lon: Double,
    val likes: Array<Like>?,
    val comments: Array<Comment>?,
)