package com.example.pokazimi.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import com.example.pokazimi.data.remote.model.ViewPost
import com.example.pokazimi.viewmodels.ViewPostViewModel

class ViewPostActivity(accessToken: String, refreshToken: String): ComponentActivity() {
    private var viewPostViewModel = ViewPostViewModel(accessToken, refreshToken)

    @Override
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    fun getPost(postId: Long, userId: Long): ViewPost? {
        return viewPostViewModel.getPost(postId, userId);
    }
}