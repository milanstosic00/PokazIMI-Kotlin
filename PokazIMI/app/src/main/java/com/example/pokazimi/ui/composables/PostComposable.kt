package com.example.pokazimi.ui.composables

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.pokazimi.R
import com.example.pokazimi.destinations.MapScreenDestination
import com.example.pokazimi.destinations.ViewPostScreenDestination
import com.example.pokazimi.ui.screens.ViewPostScreen
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun Post(navController: NavHostController, navigator: DestinationsNavigator, username: String = "username", description: String = "Description", image: Bitmap?, content: Bitmap?, lat: Double, lon: Double, postId: Long) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(10.dp, 0.dp, 10.dp, 10.dp)),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 5.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                PostHeader(navController, navigator, username, description, image)
                Spacer(modifier = Modifier.height(10.dp))
                PostContent(navigator, content!!, postId)
                PostFooter(navigator, lat, lon, postId)
            }
        }
    }
}

@Composable
fun PostHeader(navController: NavHostController, navigator: DestinationsNavigator, username: String, description: String = "Description", image: Bitmap?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(10.dp)
    ) {
        Column (
            modifier = Modifier
                .weight(1f)
        ) {
            CircularImage(image)
        }

        Column(
            modifier = Modifier
                .weight(5f)
                .padding(5.dp)
        ) {
            Text(text = "@"+username, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = "1 hour ago", fontSize = 10.sp, fontWeight = FontWeight.Light)
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .padding(start = 10.dp)
    ) {
        Text(text = description, fontSize = 14.sp)
    }
}

@Composable
fun PostContent(navigator: DestinationsNavigator, content: Bitmap, postId: Long) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 330.dp)
            .padding(horizontal = 10.dp)
            .clickable {
                navigator.navigate(ViewPostScreenDestination(postId = postId))
            }
    ) {
        Image(
            content.asImageBitmap(),
            contentDescription = "Image",
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun PostFooter(navigator: DestinationsNavigator, lat: Double, lon: Double, postId: Long) {
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "Like", Modifier.size(30.dp))
            }
        }
        Column(
            modifier = Modifier
                .weight(2f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { navigator.navigate(ViewPostScreenDestination(postId = postId)) }) {
                Icon(imageVector = Icons.Outlined.Comment, contentDescription = "Comment", Modifier.size(30.dp))
            }
        }
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { navigator.navigate(MapScreenDestination(viewingPost = true, longitude = lon.toFloat(), latitude = lat.toFloat())) }) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "View Location", Modifier.size(30.dp))
            }
        }

    }
}

@Composable
fun CircularImage(image: Bitmap?) {
    Image(
        image!!.asImageBitmap(),
        contentDescription = "Image",
        modifier = Modifier
            .height(50.dp)
            .width(50.dp)
            .clip(CircleShape)
    )
}