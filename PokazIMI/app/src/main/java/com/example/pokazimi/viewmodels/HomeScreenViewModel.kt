package com.example.pokazimi.viewmodels

import androidx.lifecycle.ViewModel
import com.example.pokazimi.data.remote.dto.Post
import com.example.pokazimi.data.remote.services.HomeService
import kotlinx.coroutines.runBlocking

class HomeScreenViewModel: ViewModel() {
    private var homeService = HomeService.create()

    fun getFeaturedPosts(userId: Long): Array<Post>?
    {
        return runBlocking { homeService.getFeaturedPosts(userId) }
    }

    fun getFollowingPosts(userId: Long): Array<Post>?
    {
        return runBlocking { homeService.getFollowingPosts(userId) }
    }
}