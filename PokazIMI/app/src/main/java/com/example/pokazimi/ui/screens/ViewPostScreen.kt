package com.example.pokazimi.ui.screens

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pokazimi.R
import com.example.pokazimi.data.remote.dto.User
import com.example.pokazimi.destinations.MapScreenDestination
import com.example.pokazimi.ui.activity.PostActivity
import com.example.pokazimi.ui.activity.ProfileActivity
import com.example.pokazimi.ui.composables.CircularImage
import com.example.pokazimi.viewmodels.PostViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ViewPostScreen(navController: NavHostController, navigator: DestinationsNavigator, postId: Int = -1) {
    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colors.background
    SideEffect {
        systemUiController.setStatusBarColor(color)
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        Header(navController)
        PostImage()
        PostInfo(navigator)
        Divide()
        CommentSection()
        Spacer(modifier = Modifier.height(125.dp))
    }
}

@Composable
fun Header(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(PaddingValues(0.dp, 10.dp, 10.dp, 10.dp)),
        horizontalArrangement = Arrangement.Start
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
}

@Composable
fun PostImage() {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.test_img),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun PostInfo(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    val myImage: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), android.R.mipmap.sym_def_app_icon)
    val result = remember {
        mutableStateOf<Bitmap>(myImage)
    }

    val profileActivity = ProfileActivity()

    val postActivity = PostActivity()

    val usernameAndProfilePic = postActivity.getUsernameAndProfilePic(1)


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(10.dp)
    ) {
        Column (
            modifier = Modifier
                .weight(1f)
        ) {
            //CircularImage()
        }

        Column(
            modifier = Modifier
                .weight(4f)
                .padding(5.dp)
        ) {
            if (usernameAndProfilePic != null) {
                Text(text = usernameAndProfilePic.username, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            Text(text = "1 hour ago", fontSize = 10.sp, fontWeight = FontWeight.Light)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp)
                .height(75.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.height(30.dp))
            {
                IconButton(
                    onClick = { navigator.navigate(MapScreenDestination(viewingPost = true, longitude = 20.90730f, latitude = 44.01750f)) }
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "View Location",
                        Modifier.size(30.dp)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp)
                .height(75.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.height(30.dp))
            {
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "Like", Modifier.size(30.dp))
                }
            }
            Row (
                modifier = Modifier
                    .height(20.dp)
                    .absoluteOffset(y = 2.dp)
            ) {
                Text(text = "32", fontSize = 12.sp)
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .absoluteOffset(y = (-10).dp)
            .padding(horizontal = 10.dp)
    ) {
        Text(text = "Neki test cisto da se popuni prostor", fontSize = 14.sp)
    }
}

@Composable
fun CommentSection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        Text(text = "Comments", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.absoluteOffset(y = (-5).dp))
        Comment(userId = 0, text = "Text text text text text text text text text text text text text text text text text text text text text text")
        Comment(userId = 0, text = "Text text text text text text text text text text text text text text text text text text text text text text")
        Comment(userId = 0, text = "Text text text text text text text text text text text text text text text text text text text text text text")
        Comment(userId = 0, text = "Text text text text text text text text text text text text text text text text text text text text text text")
        Comment(userId = 0, text = "Text text text text text text text text text text text text text text text text text text text text text text")
        Comment(userId = 0, text = "Text text text text text text text text text text text text text text text text text text text text text text")
        NewComment()
    }
}

@Composable
fun Comment(userId: Int, text : String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            //CircularImage()
        }
        Column(
            modifier = Modifier.weight(4f)
        ) {
            Text(text = "@username", fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.absoluteOffset(y = (-6).dp))
            Text(text = text, fontSize = 12.sp, fontWeight = FontWeight.Light, modifier = Modifier.absoluteOffset(y = (-7).dp), maxLines = 2)
        }
    }
}

@Composable
fun NewComment() {
    var commentText by remember {
        mutableStateOf(TextFieldValue(""))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .height(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = Icons.Outlined.Comment, contentDescription = "New Comment", Modifier.size(30.dp))
        }
        Column(
            modifier = Modifier.weight(5f)
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .border(
                        BorderStroke(1.dp, MaterialTheme.colors.onSurface),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(6.dp),
                value = commentText,
                onValueChange = { commentText = it },
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(

                    ) {
                        if(commentText.text.isEmpty()) {
                            Text("Write your comment")
                        }
                        innerTextField()
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .height(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.Send, contentDescription = "Send Comment", Modifier.size(30.dp))
            }
        }


    }
}


