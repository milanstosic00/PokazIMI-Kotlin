package com.example.pokazimi.data.remote.services.implementations

import com.example.pokazimi.data.remote.HttpRoutes
import com.example.pokazimi.data.remote.dto.MessageResponse
import com.example.pokazimi.data.remote.dto.PostRequest
import com.example.pokazimi.data.remote.dto.PostResponse
import com.example.pokazimi.data.remote.services.PostsService
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*

class PostsServiceImpl(private val client: HttpClient): PostsService {
    override suspend fun getPosts(): List<PostResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun createPost(postRequest: PostRequest): Boolean {
        return try {
            val response: HttpResponse = client.submitFormWithBinaryData(
                url = HttpRoutes.SAVE_POST,
                formData = formData {
                    append("user", postRequest.user)
                    append("description", postRequest.description)
                    append("image", postRequest.image, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=image.png")
                    })
                }
            )
            true
        } catch (e: Exception) {
            print("Error : ${e.message}")
            false
        }
    }

}