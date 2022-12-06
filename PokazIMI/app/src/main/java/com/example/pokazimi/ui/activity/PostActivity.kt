package com.example.pokazimi.ui.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Base64
import androidx.activity.ComponentActivity
import com.example.pokazimi.data.remote.dto.CommentRequest
import com.example.pokazimi.data.remote.dto.LikeRequest
import com.example.pokazimi.data.remote.model.Comment
import com.example.pokazimi.data.remote.model.Like
import com.example.pokazimi.data.remote.model.UsernameAndProfilePic
import com.example.pokazimi.viewmodels.PostViewModel
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream


class PostActivity(accessToken: String, refreshToken: String): ComponentActivity() {
    private var postViewModel = PostViewModel(accessToken, refreshToken)

    fun create_pfp(image: String?): Bitmap?
    {
        var profilePic: ByteArray
        var bmp: Bitmap
        if(image != null) {
            profilePic = image.toByteArray()
            profilePic = Base64.decode(profilePic, Base64.DEFAULT)

            bmp = BitmapFactory.decodeByteArray(profilePic, 0, profilePic.size)
            return bmp
        }

        return null
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    fun savePost(userId: Long, description: String, image: Bitmap, lat: Double, lon: Double)
    {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 30, stream)
        val imageByteArray = stream.toByteArray()
        postViewModel.savePost(userId,description,imageByteArray, lat, lon)
    }

    fun getUsernameAndProfilePic(userId: Long): UsernameAndProfilePic?
    {
        return runBlocking { postViewModel.getUsernameAndProfilePic(userId) }
    }

    fun likePost(like: LikeRequest)
    {
        postViewModel.likePost(like)
    }

    fun comment(commentRequest: CommentRequest)
    {
        postViewModel.comment(commentRequest)
    }

    fun deletePost(postId: Long)
    {
        postViewModel.deletePost(postId)
    }

    fun deleteComment(commentId: Long)
    {
        postViewModel.deleteComment(commentId)
    }

    fun deleteLike(postId: Long)
    {
        postViewModel.deleteLike(postId)
    }
}