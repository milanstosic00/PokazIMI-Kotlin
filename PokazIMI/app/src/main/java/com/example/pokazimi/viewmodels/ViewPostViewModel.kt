package com.example.pokazimi.viewmodels;

import androidx.lifecycle.ViewModel
import com.example.pokazimi.data.remote.model.ViewPost
import com.example.pokazimi.data.remote.services.PostsService
import kotlinx.coroutines.runBlocking

class ViewPostViewModel: ViewModel() {
    private var postsService = PostsService.create()

    fun getPost(postId: Long): ViewPost? {
        return runBlocking { postsService.getPost(postId) }
    }
}
