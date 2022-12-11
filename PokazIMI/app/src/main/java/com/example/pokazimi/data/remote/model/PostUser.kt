package com.example.pokazimi.data.remote.model

@kotlinx.serialization.Serializable
data class PostUser(
    val id: Long,
    val followedByUser: Boolean,
    val numberOfFollowers: Int,
    val numberOfFollowing: Int
)
