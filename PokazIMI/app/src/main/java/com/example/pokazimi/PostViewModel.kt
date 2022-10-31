package com.example.pokazimi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokazimi.data.remote.dto.PostRequest
import com.example.pokazimi.data.remote.services.PostsService
import kotlinx.coroutines.launch

class PostViewModel: ViewModel() {

    private var postsService = PostsService.create()

    fun savePost(description: String, image: ByteArray)
    {
        viewModelScope.launch { postsService.createPost(PostRequest(description,image)) }
    }
}