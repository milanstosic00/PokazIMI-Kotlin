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
                client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
                install(HttpTimeout) {
                    requestTimeoutMillis = 25000L
                }
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Log.i("Logging", message)
                        }
                    }
                    level = LogLevel.ALL
                }
                install(ResponseObserver) {
                    onResponse {}
                }
            }
            )
        }
    }


}