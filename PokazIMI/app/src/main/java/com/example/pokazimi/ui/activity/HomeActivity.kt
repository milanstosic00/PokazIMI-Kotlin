package com.example.pokazimi.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import com.example.pokazimi.data.remote.model.FeedPost
import com.example.pokazimi.data.remote.model.ViewPost
import com.example.pokazimi.viewmodels.HomeScreenViewModel
import kotlinx.coroutines.runBlocking

class HomeActivity(accessToken: String, refreshToken: String): ComponentActivity() {
    private var homeViewModel = HomeScreenViewModel(accessToken, refreshToken)

    @Override
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    fun getFeaturedPosts(filter: String): Array<FeedPost>?
    {
        return homeViewModel.getFeaturedPosts(filter)
    }

    fun getFollowingPosts(filter: String): Array<FeedPost>?
    {
        return homeViewModel.getFollowingPosts(filter)
    }

    fun getSearchPosts(latitude: Double, longitude: Double, radius: Double, filter: String): Array<FeedPost>?
    {
        return homeViewModel.getSearchPosts(latitude, longitude, radius, filter)
    }
}