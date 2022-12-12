package com.example.pokazimi.ui.screens

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pokazimi.NoPosts
import com.example.pokazimi.Sort
import com.example.pokazimi.data.remote.model.FeedPost
import com.example.pokazimi.data.remote.model.ViewPost
import com.example.pokazimi.readFileAsLinesUsingUseLines
import com.example.pokazimi.ui.activity.HomeActivity
import com.example.pokazimi.ui.activity.PostActivity
import com.example.pokazimi.ui.composables.Post
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.runBlocking
import java.io.File

@Composable
fun SearchResultScreen(navController: NavHostController, navigator: DestinationsNavigator, radius: Double, latitude: Double, longitude: Double) {
    if(navController.previousBackStackEntry?.destination == navController.currentBackStackEntry?.destination) {
        navController.popBackStack()
    }

    val context = LocalContext.current
    val path = context.getExternalFilesDir(null)!!.absolutePath
    val tempFile = File(path, "tokens.txt")
    var lines: List<String>? = null
    if(tempFile.isFile) {
        lines = readFileAsLinesUsingUseLines(tempFile)
    }
    val refreshToken = lines?.get(0)
    val accessToken = lines?.get(1)

    var searchPosts: Array<FeedPost>?
    val postActivity = PostActivity(accessToken as String, refreshToken as String)
    val homeActivity = HomeActivity(accessToken, refreshToken)

    searchPosts = homeActivity.getSearchPosts(latitude, longitude, radius)

    SearchHeader(navController = navController)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(PaddingValues(0.dp, 0.dp, 0.dp, 55.dp))
    ) {
        SearchHeader(navController = navController)
        searchPosts!!.forEach {
            val usernameAndProfilePic = runBlocking { postActivity.getUsernameAndProfilePic(it.user.id) }
            Post(navController, navigator, usernameAndProfilePic!!.username, it.description, postActivity.create_pfp(usernameAndProfilePic.profilePicture), convert(it.image0), it.lat, it.lon, it.id, it.user.id, it.time, it.likedByUser)
        }
        if(searchPosts.isEmpty()){
            NoPosts()
        }
    }
}

@Composable
fun SearchHeader(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            IconButton(
                onClick = { navController.navigateUp() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    Modifier.size(30.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(3f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Search Result",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                modifier = Modifier.absoluteOffset(y = (-2).dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Sort()
        }
    }
}