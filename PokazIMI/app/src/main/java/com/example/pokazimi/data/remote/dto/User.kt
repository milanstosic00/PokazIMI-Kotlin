package com.example.pokazimi.data.remote.dto

@kotlinx.serialization.Serializable
data class User (
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val password: String?,
    val profilePicture: String?,
    val posts: Array<Post>,
    val role: String,
    val jwtVersion: Int

)