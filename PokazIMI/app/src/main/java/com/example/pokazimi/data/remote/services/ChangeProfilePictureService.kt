package com.example.pokazimi.data.remote.services

import com.example.pokazimi.data.remote.dto.ChangeProfilePictureRequest
import com.example.pokazimi.data.remote.dto.ChangeProfilePictureResponse
import com.example.pokazimi.data.remote.dto.LogInResponse
import com.example.pokazimi.data.remote.dto.LoginRequest
import com.example.pokazimi.data.remote.services.implementations.ChangeProfilePictureServiceImpl
import com.example.pokazimi.data.remote.services.implementations.PostsServiceImpl
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.logging.*

interface ChangeProfilePictureService {
    suspend fun change(changeRequest: ChangeProfilePictureRequest): ChangeProfilePictureResponse?


    companion object {
        fun create(): ChangeProfilePictureService {
            return ChangeProfilePictureServiceImpl(
                client = HttpClient(Android) {
                    install(Logging) {
                        level = LogLevel.INFO
                    }
                }
            )
        }
    }
}