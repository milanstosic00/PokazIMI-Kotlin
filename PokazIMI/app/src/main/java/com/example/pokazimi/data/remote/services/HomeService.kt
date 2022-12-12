package com.example.pokazimi.data.remote.services

import android.content.Context
import android.view.View
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.pokazimi.MainActivity
import com.example.pokazimi.data.remote.dto.RefreshTokenRequest
import com.example.pokazimi.data.remote.model.FeedPost
import com.example.pokazimi.data.remote.model.ViewPost
import com.example.pokazimi.data.remote.services.implementations.HomeServiceImpl
import com.example.pokazimi.dataStore.Storage
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

interface HomeService {

    suspend fun getFeaturedPosts(): Array<FeedPost>?

    suspend fun getFollowingPosts(): Array<FeedPost>?

    suspend fun getSearchPosts(latitude: Double, longitude: Double, radius: Double): Array<FeedPost>?

    companion object {
        fun create(accessToken: String, refreshToken: String): HomeService {
            return HomeServiceImpl(
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