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

    fun getFeaturedPosts(): Array<FeedPost>?
    {
        return homeViewModel.getFeaturedPosts()
    }

    fun getFollowingPosts(): Array<FeedPost>?
    {
        return homeViewModel.getFollowingPosts()
    }

    fun getSearchPosts(latitude: Double, longitude: Double, radius: Double): Array<FeedPost>?
    {
        return homeViewModel.getSearchPosts(latitude, longitude, radius)
    }
}