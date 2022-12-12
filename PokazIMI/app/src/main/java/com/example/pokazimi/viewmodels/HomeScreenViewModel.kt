package com.example.pokazimi.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.pokazimi.data.remote.model.FeedPost
import com.example.pokazimi.data.remote.model.ViewPost
import com.example.pokazimi.data.remote.services.HomeService
import kotlinx.coroutines.runBlocking

class HomeScreenViewModel(accessToken: String, refreshToken: String): ViewModel() {
    private var homeService = HomeService.create(accessToken, refreshToken)

    fun getFeaturedPosts(filter: String): Array<FeedPost>?
    {
        return runBlocking { homeService.getFeaturedPosts(filter) }
    }

    fun getFollowingPosts(filter: String): Array<FeedPost>?
    {
        return runBlocking { homeService.getFollowingPosts(filter) }
    }

    fun getSearchPosts(latitude: Double, longitude: Double, radius: Double): Array<FeedPost>?
    {
        return runBlocking { homeService.getSearchPosts(latitude, longitude, radius) }
    }
}