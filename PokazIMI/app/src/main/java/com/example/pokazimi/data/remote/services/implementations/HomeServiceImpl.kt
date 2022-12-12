package com.example.pokazimi.data.remote.services.implementations

import com.example.pokazimi.data.remote.HttpRoutes
import com.example.pokazimi.data.remote.model.FeedPost
import com.example.pokazimi.data.remote.model.ViewPost
import com.example.pokazimi.data.remote.services.HomeService
import io.ktor.client.*
import io.ktor.client.request.*

class HomeServiceImpl(private val client: HttpClient): HomeService {

    override suspend fun getFeaturedPosts(filter: String): Array<FeedPost>? {
        return try {
            client.get<Array<FeedPost>>{
                url(HttpRoutes.FEATURED_POSTS + "/?filterType=$filter")
            }
        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }

    override suspend fun getFollowingPosts(filter: String): Array<FeedPost>? {
        return try {
            client.get<Array<FeedPost>>{
                url(HttpRoutes.FOLLOWING_POSTS + "/?filterType=$filter")
            }
        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }

    override suspend fun getSearchPosts(latitude: Double, longitude: Double, radius: Double): Array<FeedPost>? {
        return try {
            client.get<Array<FeedPost>>{
                url(HttpRoutes.SEARCH_POSTS + "?latitude=$latitude&longitude=$longitude&radius=$radius")
            }
        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }

}