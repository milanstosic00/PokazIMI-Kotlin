package com.example.pokazimi.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokazimi.data.remote.dto.PostRequest
import com.example.pokazimi.data.remote.services.PostsService
import kotlinx.coroutines.launch

class PostViewModel: ViewModel() {

    private var postsService = PostsService.create()

    fun savePost(userId: Long, description: String, image: ByteArray, lat: Double, lon: Double)
    {
        viewModelScope.launch { postsService.createPost(PostRequest(image,userId,description,lat,lon)) }
    }
}