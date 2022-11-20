package com.example.pokazimi.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokazimi.data.remote.dto.PostRequest
import com.example.pokazimi.data.remote.dto.UsernameAndProfilePic
import com.example.pokazimi.data.remote.services.PostsService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PostViewModel: ViewModel() {

    private var postsService = PostsService.create()

    fun savePost(userId: Long, description: String, image: ByteArray, lat: Double, lon: Double)
    {
        viewModelScope.launch { postsService.createPost(PostRequest(image,userId,description,lat,lon)) }
    }

    fun getUsernameAndProfilePic(userId: Long): UsernameAndProfilePic?
    {
        return runBlocking { postsService.getUsernameAndProfilePic(userId) }
    }
}