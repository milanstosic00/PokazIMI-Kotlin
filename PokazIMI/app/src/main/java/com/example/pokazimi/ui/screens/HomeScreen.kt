package com.example.pokazimi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pokazimi.data.remote.model.FeedPost
import com.example.pokazimi.ui.activity.HomeActivity
import com.example.pokazimi.ui.activity.PostActivity
import com.example.pokazimi.ui.composables.Post
import com.example.pokazimi.ui.screens.convert
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.runBlocking
import java.io.File

@Destination
@Composable
fun HomeScreen(navController: NavHostController, navigator: DestinationsNavigator, sort: Int = 0) {
    if(navController.previousBackStackEntry?.destination == navController.currentBackStackEntry?.destination) {
        navController.popBackStack()
    }
    var filter= "Sort by newest"
    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colors.background
    SideEffect {
        systemUiController.setStatusBarColor(color)
    }

    val following = remember {
        mutableStateOf(false)
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
    val postActivity = PostActivity(accessToken as String, refreshToken as String)
    val homeActivity = HomeActivity(accessToken, refreshToken)

    var followingPosts: Array<FeedPost>? = null
    var featuredPosts: Array<FeedPost>? = null

    if(following.value) {

        println("####################################################### $sort")
        when(sort){
            0 -> {
                followingPosts = homeActivity.getFollowingPosts("NEW")
                filter= "Sort by newest"
            }
            1 -> {
                followingPosts = homeActivity.getFollowingPosts("OLD")
                filter = "Sort by oldest"
            }
            2 -> {
                followingPosts = homeActivity.getFollowingPosts("MOST_LIKES")
                filter = "Sort by most liked"
            }
            3 -> {
                followingPosts = homeActivity.getFollowingPosts("MOST_COMMENTS")
                filter = "Sort by most comments"
            }
        }

    }
    else {
        println("####################################################### $sort")
        when(sort){
            0 -> {
                featuredPosts = homeActivity.getFeaturedPosts("NEW")
                filter= "Sort by newest"
            }
            1 -> {
                featuredPosts = homeActivity.getFeaturedPosts("OLD")
                filter= "Sort by oldest"
            }
            2 -> {
                featuredPosts = homeActivity.getFeaturedPosts("MOST_LIKES")
                filter= "Sort by most liked"
            }
            3 -> {
                featuredPosts = homeActivity.getFeaturedPosts("MOST_COMMENTS")
                filter= "Sort by most comments"
            }
    }


    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(PaddingValues(2.dp, 0.dp, 10.dp, 0.dp))
    ) {
        TextButton(
            modifier = Modifier.weight(1f).absoluteOffset(y = 3.dp),
            onClick = {
                if(following.value) following.value = !following.value
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background, contentColor = MaterialTheme.colors.onSurface),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            Text(text = "Featured", fontWeight = switchFont(!following.value))
        }
        TextButton(
            modifier = Modifier.weight(1f).absoluteOffset(y = 3.dp),
            onClick = {
                if(!following.value) following.value = !following.value
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background, contentColor = MaterialTheme.colors.onSurface),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            Text(text = "Following", fontWeight = switchFont(following.value))
        }
        Row(
            modifier = Modifier
                .weight(2f)
                .height(55.dp)
                .padding(vertical = 5.dp)
        ) {
            Button(
                onClick = { navController.navigate("map/false") },
                modifier = Modifier.width(150.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface)
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                Text(
                    text = "  Search",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Sort(navController, filter)
        }
    }

    Column(
        modifier = Modifier
            .absoluteOffset(y = 55.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        if(following.value) {
            followingPosts!!.forEach {
                val usernameAndProfilePic = runBlocking { postActivity.getUsernameAndProfilePic(it.user.id) }
                Post(navController, navigator, usernameAndProfilePic!!.username, it.description, postActivity.create_pfp(usernameAndProfilePic.profilePicture), convert(it.image0), it.lat, it.lon, it.id, it.user.id, it.time, it.likedByUser)
            }
            if(followingPosts.isEmpty()) {
                NoPosts()
            }
        }
        else {
            featuredPosts!!.forEach {
                val usernameAndProfilePic = runBlocking { postActivity.getUsernameAndProfilePic(it.user.id) }
                Post(navController, navigator, usernameAndProfilePic!!.username, it.description, postActivity.create_pfp(usernameAndProfilePic.profilePicture), convert(it.image0), it.lat, it.lon, it.id, it.user.id, it.time, it.likedByUser)
            }
            if(featuredPosts.isEmpty()) {
                NoPosts()
            }
        }

        Spacer(modifier = Modifier.height(115.dp))
    }
}

@Composable
fun NoPosts() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "Nothing to see here  ¯\\_(ツ)_/¯", fontWeight = FontWeight.Medium)
        }
    }
}

fun switchFont(bool: Boolean): FontWeight {
    if(bool) {
        return FontWeight.Bold
    }
    return FontWeight.Normal
}

@Composable
fun Sort(navController: NavHostController, filter: String) {
    val listItems = arrayOf("Sort by newest", "Sort by oldest", "Sort by most comments", "Sort by most liked")

    val selectedItem = remember {
        mutableStateOf(filter)
    }

    val expanded = remember {
        mutableStateOf(false)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.absoluteOffset(x = 6.dp)
    ) {
        IconButton(onClick = {
            expanded.value = true
        }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Open Options",
                modifier = Modifier.size(30.dp)
            )
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            listItems.forEach { itemValue ->
                DropdownMenuItem(
                    onClick = {
                        expanded.value = false
                        selectedItem.value = itemValue
                        when(selectedItem.value){
                            "Sort by newest" -> navController.navigate("home")
                            "Sort by oldest" -> navController.navigate("home/1")
                            "Sort by most liked" -> navController.navigate("home/2")
                            "Sort by most comments" -> navController.navigate("home/3")
                        }
                    },
                    enabled = true
                ) {
                    if(itemValue == selectedItem.value) {
                        Text(text = itemValue, fontWeight = FontWeight.Bold)
                    } else {
                        Text(text = itemValue)
                    }
                }
            }
        }
    }
}