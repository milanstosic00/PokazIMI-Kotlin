package com.example.pokazimi.ui.composables

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.pokazimi.R
import com.example.pokazimi.destinations.ViewPostScreenDestination
import com.example.pokazimi.ui.screens.ViewPostScreen
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun Post(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(520.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .clickable {
                    navController.navigate("viewpost")
                },
            shape = RoundedCornerShape(16.dp),
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 5.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                PostHeader()
                Spacer(modifier = Modifier.height(10.dp))
                PostContent(navController)
                PostFooter(navController)
            }
        }
    }
}

@Composable
fun PostHeader() {
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
            CircularImage()
        }

        Column(
            modifier = Modifier
                .weight(4f)
                .padding(5.dp)
        ) {
            Text(text = "@username", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = "1 hour ago", fontSize = 10.sp, fontWeight = FontWeight.Light)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "View Location", Modifier.size(30.dp))
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .padding(start = 10.dp)
    ) {
        Text(text = "Neki test cisto da se popuni prostor", fontSize = 14.sp)
    }
}

@Composable
fun PostContent(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(330.dp)
            .padding(horizontal = 10.dp)
            .clickable {
                navController.navigate("viewpost")
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.test_img),
            contentDescription = "Image",
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun PostFooter(navController: NavHostController) {
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
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
            IconButton(onClick = { navController.navigate("viewpost") }) {
                Icon(imageVector = Icons.Outlined.Comment, contentDescription = "Comment", Modifier.size(30.dp))
            }
        }
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.StarOutline, contentDescription = "Rate", Modifier.size(30.dp))
            }
        }

    }
}

@Composable
fun CircularImage() {
    Image(
        painter = painterResource(id = R.drawable.test_img),
        contentDescription = "Image",
        modifier = Modifier
            .height(50.dp)
            .width(50.dp)
            .clip(CircleShape)
    )
}