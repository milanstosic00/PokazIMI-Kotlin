package com.example.pokazimi.viewmodels;

import androidx.lifecycle.ViewModel
import com.example.pokazimi.data.remote.model.ViewPost
import com.example.pokazimi.data.remote.services.PostsService
import kotlinx.coroutines.runBlocking

class ViewPostViewModel(accessToken: String, refreshToken: String): ViewModel() {
    private var postsService = PostsService.create(accessToken, refreshToken)

    fun getPost(postId: Long): ViewPost? {
        return runBlocking { postsService.getPost(postId) }
    }
}
