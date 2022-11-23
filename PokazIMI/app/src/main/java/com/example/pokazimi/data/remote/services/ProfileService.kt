package com.example.pokazimi.data.remote.services

import com.example.pokazimi.data.remote.dto.ChangeProfilePictureRequest
import com.example.pokazimi.data.remote.dto.FollowRequest
import com.example.pokazimi.data.remote.dto.MessageResponse
import com.example.pokazimi.data.remote.dto.User
import com.example.pokazimi.data.remote.services.implementations.ProfileServiceImpl
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

interface ProfileService {
    suspend fun change(changeRequest: ChangeProfilePictureRequest): MessageResponse?

    suspend fun getUser(userId: Long): User?

    suspend fun followUser(followRequest: FollowRequest): MessageResponse?

    companion object {
        fun create(): ProfileService {
            return ProfileServiceImpl(
                client = HttpClient(Android) {
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    install(JsonFeature) {
                        serializer = KotlinxSerializer()
                    }
                }
            )
        }
    }
}