package com.example.pokazimi.data.remote

import com.example.pokazimi.data.remote.dto.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

interface RequestService {

    suspend fun getPosts(): List<PostResponse>

    suspend fun createPost(postRequest: PostRequest): PostResponse?

    suspend fun login(loginRequest: LoginRequest): MessageResponse?

    suspend fun registration(registrationRequest: RegistrationRequest): MessageResponse?

    companion object {
        fun create(): RequestService {
            return RequestServiceImpl(
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