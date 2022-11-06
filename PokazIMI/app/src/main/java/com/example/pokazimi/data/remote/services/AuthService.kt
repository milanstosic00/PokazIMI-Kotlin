package com.example.pokazimi.data.remote.services

import com.example.pokazimi.data.remote.dto.LogInResponse
import com.example.pokazimi.data.remote.dto.RefreshTokenRequest
import com.example.pokazimi.data.remote.services.implementations.AuthServiceImpl
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

interface AuthService {

    suspend fun refresh(refreshToken: RefreshTokenRequest): LogInResponse?

    companion object {
        fun create(): AuthService {
            return AuthServiceImpl(
                client = HttpClient(Android) {
                    install(Logging) {
                        level = LogLevel.INFO
                    }
                    install(JsonFeature) {
                        serializer = KotlinxSerializer()
                    }
                }
            )
        }
    }
}


