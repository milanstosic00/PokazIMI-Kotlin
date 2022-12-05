package com.example.pokazimi.data.remote.services.implementations

import com.example.pokazimi.data.remote.HttpRoutes
import com.example.pokazimi.data.remote.dto.MessageResponse
import com.example.pokazimi.data.remote.dto.RegistrationRequest
import com.example.pokazimi.data.remote.services.RegistrationService
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

class RegistrationServiceImpl(private val client: HttpClient) : RegistrationService {

    override suspend fun registration(registrationRequest: RegistrationRequest): MessageResponse {
    return try {
            client.post<MessageResponse>{
                url(HttpRoutes.REGISTER)
                contentType(ContentType.Application.Json)
                body = registrationRequest
            }
        } catch (e: ClientRequestException) {
            return MessageResponse(e.message)
        }
    }
}