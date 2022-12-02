package com.example.pokazimi.data.remote.services

import com.example.pokazimi.data.remote.dto.*
import com.example.pokazimi.data.remote.model.Comment
import com.example.pokazimi.data.remote.model.Like
import com.example.pokazimi.data.remote.model.UsernameAndProfilePic
import com.example.pokazimi.data.remote.model.ViewPost
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import com.example.pokazimi.data.remote.services.implementations.PostsServiceImpl
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.logging.*

interface PostsService {

    suspend fun getPosts(): List<PostResponse>

    suspend fun createPost(postRequest: PostRequest): Boolean?

    suspend fun like(like: Like): MessageResponse?

    suspend fun comment(commentRequest: CommentRequest): MessageResponse?

    suspend fun getUsernameAndProfilePic(userId: Long): UsernameAndProfilePic?

    suspend fun getPost(postId: Long): ViewPost?

    suspend fun deletePost(postId: Long): MessageResponse?

    suspend fun deleteComment(commentId: Long): MessageResponse?

    companion object {
        fun create(accessToken: String, refreshToken: String): PostsService {
            return PostsServiceImpl(
                client = HttpClient(Android) {
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    install(JsonFeature) {
                        serializer = KotlinxSerializer()
                    }
                    install(Auth){
                        bearer{
                            loadTokens {
                                BearerTokens(accessToken, refreshToken)
                            }
                            refreshTokens {
                                /*val dataStore = Storage(context)
                                val refreshToken = dataStore.returnRefreshToken()
                                val authService = AuthService.create()
                                val logInResponse =
                                    runBlocking { authService.refresh(RefreshTokenRequest(refreshToken)) }
                                logInResponse?.let { it1 -> BearerTokens(it1.accessToken, logInResponse.accessToken) }*/
                                BearerTokens("asd", "asd")
                            }
                        }
                    }
                }
            )
        }
    }
}