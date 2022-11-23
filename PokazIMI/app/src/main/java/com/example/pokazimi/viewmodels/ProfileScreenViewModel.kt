package com.example.pokazimi.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokazimi.data.remote.dto.ChangeProfilePictureRequest
import com.example.pokazimi.data.remote.dto.FollowRequest
import com.example.pokazimi.data.remote.dto.User
import com.example.pokazimi.data.remote.services.ProfileService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProfileScreenViewModel: ViewModel() {
    private var profileService = ProfileService.create()

    fun changeProfilePicture( userId: Long, image: ByteArray)
    {
        viewModelScope.launch { profileService.change(ChangeProfilePictureRequest(userId, image)) }
    }

    fun getUser(userId: Long): User?
    {

        return  runBlocking{ profileService.getUser(userId) }
    }

    fun followUser(userId: Long, followerId: Long)
    {
        viewModelScope.launch { profileService.followUser(FollowRequest(userId, followerId)) }
    }
}