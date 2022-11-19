package com.example.pokazimi.ui.activity

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pokazimi.viewmodels.PostViewModel
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

    fun savePost(userId: Int, description: String, image: Bitmap, lat: Double, lon: Double)
    {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val imageByteArray = stream.toByteArray()
        postViewModel.savePost(1,description,imageByteArray, lat, lon)
    }
}