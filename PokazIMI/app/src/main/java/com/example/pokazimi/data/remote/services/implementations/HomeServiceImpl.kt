package com.example.pokazimi.data.remote.services.implementations

import com.example.pokazimi.data.remote.HttpRoutes
import com.example.pokazimi.data.remote.dto.Post
import com.example.pokazimi.data.remote.services.HomeService
import io.ktor.client.*
import io.ktor.client.request.*

class HomeServiceImpl(private val client: HttpClient): HomeService {

    override suspend fun getFeaturedPosts(userId: Long): Array<Post>? {
        return try {
            client.get<Array<Post>>{
                url(HttpRoutes.FEATURED_POSTS + userId)
            }
        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }

    override suspend fun getFollowingPosts(userId: Long): Array<Post>? {
        return try {
            client.get<Array<Post>>{
                url(HttpRoutes.FOLLOWING_POSTS + userId)
            }
        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }

}