package com.example.pokazimi.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokazimi.data.remote.dto.CommentRequest
import com.example.pokazimi.data.remote.model.Comment
import com.example.pokazimi.data.remote.model.Like
import com.example.pokazimi.data.remote.dto.PostRequest
import com.example.pokazimi.data.remote.model.UsernameAndProfilePic
import com.example.pokazimi.data.remote.services.PostsService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PostViewModel(accessToken: String, refreshToken: String): ViewModel() {



    private var postsService = PostsService.create(accessToken, refreshToken)

    fun savePost(userId: Long, description: String, image: ByteArray, lat: Double, lon: Double)
    {
        viewModelScope.launch { postsService.createPost(PostRequest(image,userId,description,lat,lon)) }
    }

    fun getUsernameAndProfilePic(userId: Long): UsernameAndProfilePic?
    {
        return runBlocking { postsService.getUsernameAndProfilePic(userId) }
    }

    fun likePost(like: Like)
    {
        viewModelScope.launch { postsService.like(like) }
    }

    fun comment(commentRequest: CommentRequest)
    {
        viewModelScope.launch { postsService.comment(commentRequest) }
    }

    fun deletePost(postId: Long)
    {
        viewModelScope.launch { postsService.deletePost(postId) }
    }

}