package com.example.pokazimi.data.remote.services.implementations

import com.example.pokazimi.data.remote.HttpRoutes
import com.example.pokazimi.data.remote.dto.LogInResponse
import com.example.pokazimi.data.remote.dto.RefreshTokenRequest
import com.example.pokazimi.data.remote.services.AuthService
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthServiceImpl(private val client: HttpClient) : AuthService{
    override suspend fun refresh(refreshToken: RefreshTokenRequest): LogInResponse? {
        return try {
            client.post<LogInResponse>{
                url(HttpRoutes.REFRESH)
                contentType(ContentType.Application.Json)
                body = refreshToken
            }

        } catch (e: ClientRequestException) {
            //4xx - responses
            return LogInResponse(e.message, "Refresh token expired")

        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }
}