package com.example.pokazimi.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.pokazimi.data.remote.model.ViewPost
import com.example.pokazimi.data.remote.services.HomeService
import kotlinx.coroutines.runBlocking

class HomeScreenViewModel(accessToken: String, refreshToken: String): ViewModel() {
    private var homeService = HomeService.create(accessToken, refreshToken)

    fun getFeaturedPosts(): Array<ViewPost>?
    {
        return runBlocking { homeService.getFeaturedPosts() }
    }

    fun getFollowingPosts(): Array<ViewPost>?
    {
        return runBlocking { homeService.getFollowingPosts() }
    }

    fun getSearchPosts(latitude: Double, longitude: Double, radius: Double): Array<ViewPost>?
    {
        return runBlocking { homeService.getSearchPosts(latitude, longitude, radius) }
    }
}