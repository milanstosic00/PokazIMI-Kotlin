package com.example.pokazimi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun SearchResultScreen(navController: NavHostController, navigator: DestinationsNavigator, radius: Double, latitude: Double, longitude: Double, filter: String?) {

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

    searchPosts = filter?.let { homeActivity.getSearchPosts(latitude, longitude, radius, it) }


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
            SortResults(navController = navController, radius = radius, lat = latitude, lon = longitude, filter = filter)
        }
    }


    //SearchHeader(navController, radius, latitude, longitude, filter)
    Column(
        modifier = Modifier
            .absoluteOffset(y = 55.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        searchPosts!!.forEach {
            val usernameAndProfilePic = runBlocking { postActivity.getUsernameAndProfilePic(it.user.id) }
            Post(navController, navigator, usernameAndProfilePic!!.username, it.description, postActivity.create_pfp(usernameAndProfilePic.profilePicture), convert(it.image0), it.lat, it.lon, it.id, it.user.id, it.time, it.likedByUser)
        }
        if(searchPosts.isEmpty()){
            NoPosts()
        }

        Spacer(modifier = Modifier.height(110.dp))
    }

}

@Composable
fun SearchHeader(navController: NavHostController, radius: Double, lat: Double, lon: Double, filter: String?) {
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
            SortResults(navController = navController, radius = radius, lat = lat, lon = lon, filter = filter)
        }
    }
}

@Composable
fun SortResults(navController: NavHostController, radius: Double, lat: Double, lon: Double, filter: String?) {
    val listItems = arrayOf("Sort by newest", "Sort by oldest", "Sort by most comments", "Sort by most liked")

    var selectedItem: String = ""

    when(filter) {
        "NEW" -> selectedItem = "Sort by newest"
        "OLD" -> selectedItem = "Sort by oldest"
        "MOST_LIKES" -> selectedItem = "Sort by most liked"
        "MOST_COMMENTS" -> selectedItem = "Sort by most comments"
    }

    val expanded = remember {
        mutableStateOf(false)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.absoluteOffset(0.dp)
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

        androidx.compose.material.DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            listItems.forEach { itemValue ->
                DropdownMenuItem(
                    onClick = {
                        expanded.value = false
                        selectedItem = itemValue
                        when (selectedItem) {
                            "Sort by newest" -> navController.navigate("search/${radius.toFloat() }/${lat.toFloat()}/${lon.toFloat()}/NEW")
                            "Sort by oldest" -> navController.navigate("search/${radius.toFloat() }/${lat.toFloat()}/${lon.toFloat()}/OLD")
                            "Sort by most liked" -> navController.navigate("search/${radius.toFloat() }/${lat.toFloat()}/${lon.toFloat()}/MOST_LIKES")
                            "Sort by most comments" -> navController.navigate("search/${radius.toFloat() }/${lat.toFloat()}/${lon.toFloat()}/MOST_COMMENTS")
                        }
                    },
                    enabled = true
                ) {
                    if (itemValue == selectedItem) {
                        Text(text = itemValue, fontWeight = FontWeight.Bold)
                    } else {
                        Text(text = itemValue)
                    }
                }
            }
        }
    }
}