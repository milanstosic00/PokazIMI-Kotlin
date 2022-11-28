package com.example.pokazimi.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import com.example.pokazimi.data.remote.model.ViewPost
import com.example.pokazimi.viewmodels.HomeScreenViewModel

class HomeActivity: ComponentActivity() {
    private var homeViewModel = HomeScreenViewModel()

    @Override
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    fun getFeaturedPosts(userId: Long): Array<ViewPost>?
    {
        return homeViewModel.getFeaturedPosts(userId)
    }

    fun getFollowingPosts(userId: Long): Array<ViewPost>?
    {
        return homeViewModel.getFollowingPosts(userId)
    }
}