package com.example.pokazimi.data.remote.services

import com.example.pokazimi.data.remote.RequestService
import com.example.pokazimi.data.remote.RequestServiceImpl
import com.example.pokazimi.data.remote.dto.MessageResponse
import com.example.pokazimi.data.remote.dto.RegistrationRequest
import com.example.pokazimi.data.remote.services.implementations.RegistrationServiceImpl
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

interface RegistrationService {
    suspend fun registration(registrationRequest: RegistrationRequest): MessageResponse?


    companion object {
        fun create(): RegistrationService {
            return RegistrationServiceImpl(
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