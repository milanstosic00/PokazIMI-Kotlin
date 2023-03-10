package com.example.pokazimi.ui.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import com.example.pokazimi.data.remote.dto.User
import com.example.pokazimi.viewmodels.ProfileScreenViewModel
import java.io.ByteArrayOutputStream

class ProfileActivity: ComponentActivity() {
    private var profileViewModel = ProfileScreenViewModel()

    @Override
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }


    fun changeProfilePicture(userId: Int, image: Bitmap)
    {

        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val imageByteArray = stream.toByteArray()

        profileViewModel.changeProfilePicture(1,imageByteArray)
    }

    fun getUser(userId: Long): User?
    {
        return profileViewModel.getUser(userId)
    }
}