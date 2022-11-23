package com.example.pokazimi.data.remote.dto

@kotlinx.serialization.Serializable
data class FollowRequest(
    val userId: Long,
    val followerId: Long
)
