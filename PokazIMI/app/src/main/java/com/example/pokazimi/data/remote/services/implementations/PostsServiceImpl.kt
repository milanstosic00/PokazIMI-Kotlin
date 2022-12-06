package com.example.pokazimi.data.remote.services.implementations

import com.example.pokazimi.data.remote.HttpRoutes
import com.example.pokazimi.data.remote.dto.*
import com.example.pokazimi.data.remote.model.Like
import com.example.pokazimi.data.remote.model.Comment
import com.example.pokazimi.data.remote.model.UsernameAndProfilePic
import com.example.pokazimi.data.remote.model.ViewPost
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
                    append("lat", postRequest.lat)
                    append("lon", postRequest.lon)
                }
            )
            true
        } catch (e: Exception) {
            print("Error : ${e.message}")
            false
        }
    }

    override suspend fun like(like: LikeRequest): MessageResponse? {
        return try {
            client.post<MessageResponse> {
                url(HttpRoutes.LIKE + "?post=${like.post}&likersId=${like.likersId}")
                contentType(ContentType.Application.Json)
            }
        }catch (e: Exception) {
                print("Error : ${e.message}")
                null
        }
    }

    override suspend fun comment(commentRequest: CommentRequest): MessageResponse? {
        return try {
            client.post<MessageResponse> {
                url(HttpRoutes.COMMENT + "?post=" + commentRequest.post_id + "&content=" + commentRequest.content + "&commentersId=" + commentRequest.commentersId)
                contentType(ContentType.Application.Json)
            }
        }catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }

    override suspend fun getUsernameAndProfilePic(userId: Long): UsernameAndProfilePic? {
        return try {
            client.get<UsernameAndProfilePic>{
                url(HttpRoutes.GET_USERNAME_PROFILEPIC + "/$userId")
            }
        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }

    override suspend fun getPost(postId: Long, userId: Long): ViewPost? {
        return try {
            client.get<ViewPost>{
                url(HttpRoutes.GET_POST + "/postId=$postId/userId=$userId")
            }
        }
        catch (e: Exception) {
            print("Error")
            null
        }
    }

    override suspend fun deletePost(postId: Long): MessageResponse? {
        return try {
            client.delete<MessageResponse>{
                url(HttpRoutes.DELETE_POST + "/$postId")
            }
        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }

    override suspend fun deleteComment(commentId: Long): MessageResponse? {
        return try {
            client.delete<MessageResponse>{
                url(HttpRoutes.DELETE_COMMENT + "/$commentId")
            }
        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }

    override suspend fun deleteLike(postId: Long): MessageResponse? {
        return try {
            client.delete<MessageResponse>{
                url(HttpRoutes.DELETE_LIKE + "/$postId")
            }
        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }
}