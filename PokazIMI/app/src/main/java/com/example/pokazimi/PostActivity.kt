package com.example.pokazimi

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pokazimi.data.remote.dto.PostRequest
import com.example.pokazimi.data.remote.services.PostsService
import com.example.pokazimi.ui.theme.PokazIMITheme
import com.ramcosta.composedestinations.DestinationsNavHost
import java.io.ByteArrayOutputStream

class PostActivity: ComponentActivity() {
    private lateinit var description: String
    private lateinit var image: ByteArray
    private lateinit var imgView: ImageView
    private var postViewModel = PostViewModel()

    val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
        if (uri != null) {
            imgView.setImageURI(uri)
            val bitmap = (imgView.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            image = stream.toByteArray()
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    fun chooseImage(){

        pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

    }

    fun chooseDescription(newDescription: String){
        description = newDescription
    }

    fun savePost()
    {
        postViewModel.savePost(description,image)
    }
}