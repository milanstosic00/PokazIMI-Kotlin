package com.example.pokazimi.data.remote.dto
@kotlinx.serialization.Serializable
data class UsernameAndProfilePic (
    val username: String,
    val profilePicture: String?
)