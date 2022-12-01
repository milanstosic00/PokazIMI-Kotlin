package com.example.pokazimi.data.remote.model

@kotlinx.serialization.Serializable
data class User (
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val profilePicture: String?,
    val posts: Array<Post>,
    val role: String,
    val jwtVersion: Int

)