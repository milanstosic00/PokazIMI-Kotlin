package com.example.pokazimi.data.remote.services

import com.example.pokazimi.data.remote.dto.ChangeProfilePictureRequest
import com.example.pokazimi.data.remote.dto.FollowRequest
import com.example.pokazimi.data.remote.dto.MessageResponse
import com.example.pokazimi.data.remote.model.User
import com.example.pokazimi.data.remote.services.implementations.ProfileServiceImpl
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

interface ProfileService {
    suspend fun change(changeRequest: ChangeProfilePictureRequest): MessageResponse?

    suspend fun getUser(userId: Long): User?

    suspend fun followUser(followRequest: FollowRequest): MessageResponse?
    suspend fun unfollowUser(followRequest: FollowRequest): MessageResponse?

    companion object {
        fun create(accessToken: String, refreshToken: String): ProfileService {
            return ProfileServiceImpl(
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