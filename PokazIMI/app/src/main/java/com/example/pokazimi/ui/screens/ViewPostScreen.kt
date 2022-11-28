package com.example.pokazimi.ui.screens

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pokazimi.data.remote.dto.CommentRequest
import com.example.pokazimi.data.remote.model.ViewPost
import com.example.pokazimi.destinations.MapScreenDestination
import com.example.pokazimi.ui.activity.PostActivity
import com.example.pokazimi.ui.activity.ViewPostActivity
import com.example.pokazimi.ui.composables.CircularImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ViewPostScreen(navController: NavHostController, navigator: DestinationsNavigator, postId: Long = -1) {
    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colors.background
    SideEffect {
        systemUiController.setStatusBarColor(color)
    }

    val viewPostActivity = ViewPostActivity()
    val post = viewPostActivity.getPost(postId)
    var likes = 0
    if (post != null) {
        likes = if(post.likes != null) {
            post.likes.size
        } else {
            0
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        Header(navController)
        create_img(post)?.let { PostImage(it) }
        if (post != null) {
            PostInfo(navigator, post.lat, post.lon, likes, post.description, post.user.id)
            Divide()
            CommentSection(post.id)
        }
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
fun PostImage(image: Bitmap) {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
    ) {
        Image(
            bitmap = image.asImageBitmap(),
            contentDescription = "Image",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun PostInfo(navigator: DestinationsNavigator, lat: Double, lon: Double, likes: Int, description: String, userId: Long) {
    val context = LocalContext.current
    val myImage: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), android.R.mipmap.sym_def_app_icon)
    val result = remember {
        mutableStateOf<Bitmap>(myImage)
    }

    val postActivity = PostActivity()
    val usernameAndProfilePic = postActivity.getUsernameAndProfilePic(userId)

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
            CircularImage(convert(usernameAndProfilePic!!.profilePicture))
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
                    onClick = { navigator.navigate(MapScreenDestination(viewingPost = true, longitude = lon.toFloat(), latitude = lat.toFloat())) }
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
                Text(text = likes.toString(), fontSize = 12.sp)
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .absoluteOffset(y = (-10).dp)
            .padding(horizontal = 10.dp)
    ) {
        Text(text = description, fontSize = 14.sp)
    }
}

@Composable
fun CommentSection(postId: Long) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        Text(text = "Comments", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.absoluteOffset(y = (-5).dp))
        CommentComposable(userId = 0, text = "Text text text text text text text text text text text text text text text text text text text text text text")
        NewComment(postId)
    }
}

@Composable
fun CommentComposable(userId: Int, text : String) {
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
fun NewComment(postId: Long) {
    var commentText by remember {
        mutableStateOf(TextFieldValue(""))
    }

    val postActivity = PostActivity()

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
                    Row() {
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
            IconButton(onClick = {
                val comment = CommentRequest(post_id = postId, content = commentText.text, commentersId = 2)
                println(comment)
                postActivity.comment(comment)
            }) {
                Icon(imageVector = Icons.Outlined.Send, contentDescription = "Send Comment", Modifier.size(30.dp))
            }
        }


    }
}

fun create_img(post: ViewPost?): Bitmap?
{
    var contentPic: ByteArray
    val bmp: Bitmap
    if(post != null) {
        if(post.image != null) {
            contentPic = post.image.toByteArray()
            contentPic = Base64.decode(contentPic, Base64.DEFAULT)

            bmp = BitmapFactory.decodeByteArray(contentPic, 0, contentPic.size)
            return bmp
        }
    }

    return null
}

fun convert(img: String?): Bitmap?
{
    var pic: ByteArray
    val bmp:Bitmap

    pic = img!!.toByteArray()
    pic = Base64.decode(pic, Base64.DEFAULT)

    bmp = BitmapFactory.decodeByteArray(pic, 0, pic.size)
    return bmp
}
