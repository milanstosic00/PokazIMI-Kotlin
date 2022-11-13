package com.example.pokazimi.data.remote.dto

@kotlinx.serialization.Serializable
data class ChangeProfilePictureRequest(
    val userId: Long,
    val profilePicture: ByteArray
)