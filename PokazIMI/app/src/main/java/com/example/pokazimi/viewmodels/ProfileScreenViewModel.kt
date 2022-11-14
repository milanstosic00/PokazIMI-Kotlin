package com.example.pokazimi.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokazimi.data.remote.dto.ChangeProfilePictureRequest
import com.example.pokazimi.data.remote.services.ChangeProfilePictureService
import kotlinx.coroutines.launch

class ProfileScreenViewModel: ViewModel() {
    private var profilePictureService = ChangeProfilePictureService.create()

    fun changeProfilePicture( userId: Long, image: ByteArray)
    {
        viewModelScope.launch { profilePictureService.change(ChangeProfilePictureRequest(userId, image)) }
    }
}