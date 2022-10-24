package com.example.pokazimi.data.remote

import com.example.pokazimi.data.remote.dto.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

class RequestServiceImpl(private val client: HttpClient) : RequestService{

    override suspend fun getPosts(): List<PostResponse> {
        return try {
            client.get { url(HttpRoutes.POSTS) }
        } catch (e: RedirectResponseException) {
            //3xx - responses
            print("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ClientRequestException) {
            //4xx - responses
            print("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ServerResponseException) {
            //5xx - responses
            print("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: Exception) {
            //No internet / General error
            print("Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun createPost(postRequest: PostRequest): PostResponse? {
        return try {
            client.post<PostResponse> {
                url(HttpRoutes.POSTS)
                contentType(ContentType.Application.Json)
                body = postRequest
            }
        } catch (e: RedirectResponseException) {
            //3xx - responses
            print("Error: ${e.response.status.description}")
            null
        } catch (e: ClientRequestException) {
            //4xx - responses
            print("Error: ${e.response.status.description}")
            null
        } catch (e: ServerResponseException) {
            //5xx - responses
            print("Error: ${e.response.status.description}")
            null
        } catch (e: Exception) {
            //No internet / General error
            print("Error: ${e.message}")
            null
        }
    }

    override suspend fun login(loginRequest: LoginRequest): MessageResponse? {
        return try {
            client.post<MessageResponse>{
                url(HttpRoutes.LOGIN)
                contentType(ContentType.Application.Json)
                body = loginRequest
            }
        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }

    override suspend fun registration(registrationRequest: RegistrationRequest): MessageResponse? {
        return try {
            client.post<MessageResponse>{
                url(HttpRoutes.LOGIN)
                contentType(ContentType.Application.Json)
                body = registrationRequest
            }
        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }
}