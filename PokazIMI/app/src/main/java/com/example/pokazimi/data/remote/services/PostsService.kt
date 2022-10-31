package com.example.pokazimi.data.remote.services

import com.example.pokazimi.data.remote.RequestService
import com.example.pokazimi.data.remote.RequestServiceImpl
import com.example.pokazimi.data.remote.dto.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

interface PostsService {

    suspend fun getPosts(): List<PostResponse>

    suspend fun createPost(postRequest: PostRequest): MessageResponse?

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