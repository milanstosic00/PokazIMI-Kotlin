package com.example.pokazimi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pokazimi.data.remote.dto.Post
import com.example.pokazimi.destinations.MapScreenDestination
import com.example.pokazimi.ui.activity.HomeActivity
import com.example.pokazimi.ui.activity.PostActivity
import com.example.pokazimi.ui.composables.Post
import com.example.pokazimi.ui.screens.create_content
import com.example.pokazimi.ui.screens.create_image
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.runBlocking

@Destination
@Composable
fun HomeScreen(navController: NavHostController, navigator: DestinationsNavigator) {
    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colors.background
    SideEffect {
        systemUiController.setStatusBarColor(color)
    }

    val postActivity = PostActivity()
    val homeActivity = HomeActivity()

    //var featuredPosts: Array<Post>? = homeActivity.getFeaturedPosts(1)
    var followingPosts: Array<Post>? = homeActivity.getFollowingPosts(2)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Button(
            onClick = { navigator.navigate(MapScreenDestination(viewingPost = false)) },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface)
        ) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            Text(
                text = "  Search here",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }

    Column(
        modifier = Modifier
            .absoluteOffset(y = 55.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val usernameAndProfilePic = runBlocking { postActivity.getUsernameAndProfilePic(1) }
        followingPosts!!.forEach {
            Post(navController, navigator, usernameAndProfilePic!!.username, it.description, postActivity.create_pfp(usernameAndProfilePic.profilePicture), create_content(it), it.lat, it.lon, it.id)
        }
        Spacer(modifier = Modifier.height(115.dp))
    }
}