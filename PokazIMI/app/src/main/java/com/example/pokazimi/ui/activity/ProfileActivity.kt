package com.example.pokazimi.ui.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import com.example.pokazimi.data.remote.model.User
import com.example.pokazimi.viewmodels.ProfileScreenViewModel
import java.io.ByteArrayOutputStream

class ProfileActivity(accessToken: String, refreshToken: String): ComponentActivity() {
    private var profileViewModel = ProfileScreenViewModel(accessToken, refreshToken)

    @Override
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }


    fun changeProfilePicture(userId: Long, image: Bitmap)
    {

        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val imageByteArray = stream.toByteArray()

        profileViewModel.changeProfilePicture(userId,imageByteArray)
    }

    fun getUser(userId: Long): User?
    {
        return profileViewModel.getUser(userId)
    }

    fun followUser(userId: Long, followerId: Long)
    {
        profileViewModel.followUser(userId, followerId)
    }
}