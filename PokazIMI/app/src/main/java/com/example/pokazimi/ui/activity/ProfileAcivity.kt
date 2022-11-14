package com.example.pokazimi.ui.activity

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pokazimi.viewmodels.PostViewModel
import com.example.pokazimi.viewmodels.ProfileScreenViewModel
import java.io.ByteArrayOutputStream

class ProfileAcivity: ComponentActivity() {
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
}