package com.example.pokazimi.data.remote.services.implementations

import com.example.pokazimi.data.remote.HttpRoutes
import com.example.pokazimi.data.remote.dto.LogInResponse
import com.example.pokazimi.data.remote.dto.LoginRequest
import com.example.pokazimi.data.remote.dto.MessageResponse
import com.example.pokazimi.data.remote.services.LogInService
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class LogInServiceImpl(private val client: HttpClient) : LogInService {


    override suspend fun login(loginRequest: LoginRequest): LogInResponse? {
        return try {
            client.post<LogInResponse>{
                url(HttpRoutes.LOGIN)
                contentType(ContentType.Application.Json)
                body = loginRequest
            }
        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }
}