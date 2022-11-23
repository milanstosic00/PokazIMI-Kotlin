package com.example.pokazimi.data.remote.services

import com.example.pokazimi.data.remote.dto.Post
import com.example.pokazimi.data.remote.services.implementations.HomeServiceImpl
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

interface HomeService {

    suspend fun getFeaturedPosts(userId: Long): Array<Post>?

    suspend fun getFollowingPosts(userId: Long): Array<Post>?

    companion object {
        fun create(): HomeService {
            return HomeServiceImpl(
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