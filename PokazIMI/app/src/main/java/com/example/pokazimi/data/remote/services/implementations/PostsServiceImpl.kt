package com.example.pokazimi.data.remote.services.implementations

import com.example.pokazimi.data.remote.HttpRoutes
import com.example.pokazimi.data.remote.dto.MessageResponse
import com.example.pokazimi.data.remote.dto.PostRequest
import com.example.pokazimi.data.remote.dto.PostResponse
import com.example.pokazimi.data.remote.services.PostsService
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class PostsServiceImpl(private val client: HttpClient): PostsService {
    override suspend fun getPosts(): List<PostResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun createPost(postRequest: PostRequest): MessageResponse? {
        return try {
            client.post<MessageResponse>{
                url(HttpRoutes.LOGIN)
                contentType(ContentType.Application.Json)
                body = PostRequest
            }
        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }

}