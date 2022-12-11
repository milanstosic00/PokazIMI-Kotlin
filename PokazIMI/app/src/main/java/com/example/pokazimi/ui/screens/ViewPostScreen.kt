package com.example.pokazimi.ui.screens

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pokazimi.data.remote.dto.CommentRequest
import com.example.pokazimi.data.remote.dto.LikeRequest
import com.example.pokazimi.data.remote.model.Comment
import com.example.pokazimi.data.remote.model.Post
import com.example.pokazimi.data.remote.model.ViewPost
import com.example.pokazimi.destinations.MapScreenDestination
import com.example.pokazimi.getUserId
import com.example.pokazimi.readFileAsLinesUsingUseLines
import com.example.pokazimi.ui.activity.PostActivity
import com.example.pokazimi.ui.activity.ViewPostActivity
import com.example.pokazimi.ui.composables.CircularImage
import com.google.accompanist.pager.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.io.File
import kotlin.math.absoluteValue

@Destination
@Composable
fun ViewPostScreen(navController: NavHostController, navigator: DestinationsNavigator, postId: Long = -1, userId: Long) {
    val context = LocalContext.current
    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colors.background
    SideEffect {
        systemUiController.setStatusBarColor(color)
    }

    val path = context.getExternalFilesDir(null)!!.absolutePath
    val tempFile = File(path, "tokens.txt")
    var lines: List<String>? = null
    if(tempFile.isFile) {
        lines = readFileAsLinesUsingUseLines(tempFile)
    }

    val refreshToken = lines?.get(0)
    val accessToken = lines?.get(1)

    val postActivity = PostActivity(accessToken as String, refreshToken as String)
    val viewPostActivity = ViewPostActivity(accessToken, refreshToken)
    val post = viewPostActivity.getPost(postId, getUserId())
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
        Header(navController, post!!.user.id, postActivity, post.id)
        create_img(post)?.let { PostImage(it) }
        PostInfo(navController, navigator, post.id, post.lat, post.lon, likes, post.description, post.user.id, post.likedByUser)
        Divide()
        CommentSection(post.id, userId, post.comments, postActivity, navController)
        Spacer(modifier = Modifier.height(125.dp))
    }
}

@Composable
fun Header(navController: NavHostController, userId: Long, postActivity: PostActivity, postId: Long) {
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
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            if(userId == getUserId()) {
                IconButton(
                    onClick = {
                        postActivity.deletePost(postId)
                        navController.navigateUp() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete",
                        Modifier.size(30.dp)
                    )
                }
            }
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
fun PostInfo(navController: NavHostController, navigator: DestinationsNavigator, postId: Long, lat: Double, lon: Double, likes: Int, description: String, userId: Long, liked: Boolean) {
    val context = LocalContext.current

    val userIdFromJWT = getUserId()

    val like = remember {
        mutableStateOf(liked)
    }

    val numLikes = remember {
        mutableStateOf(likes)
    }

    val path = context.getExternalFilesDir(null)!!.absolutePath
    val tempFile = File(path, "tokens.txt")
    var lines: List<String>? = null
    if(tempFile.isFile) {
        lines = readFileAsLinesUsingUseLines(tempFile)
    }

    val refreshToken = lines?.get(0)
    val accessToken = lines?.get(1)

    val postActivity = PostActivity(accessToken as String, refreshToken as String)
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
            CircularImage(navController, convert(usernameAndProfilePic!!.profilePicture), userId)
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
                if(!like.value) {
                    IconButton(onClick = {
                        like.value = !like.value
                        numLikes.value++
                        postActivity.likePost(LikeRequest(postId, userIdFromJWT))
                    }) {
                        Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "Like", Modifier.size(30.dp))
                    }
                }
                else {
                    IconButton(onClick = {
                        like.value = !like.value
                        numLikes.value--
                        postActivity.deleteLike(postId)
                    }) {
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = "Like", Modifier.size(30.dp), tint = Color.Red)
                    }
                }
            }
            Row (
                modifier = Modifier
                    .height(20.dp)
                    .absoluteOffset(y = 2.dp)
            ) {
                Text(text = numLikes.value.toString(), fontSize = 12.sp)
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
fun CommentSection(postId: Long, userId:Long, comments: Array<Comment>?, postActivity: PostActivity, navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        Text(text = "Comments", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.absoluteOffset(y = (-5).dp))

        comments!!.forEach {
            CommentComposable(userId = it.commentersId, text = it.content, postActivity = postActivity, navController = navController, commentId = it.id)
        }

        NewComment(postId, userId, navController)
    }
}

@Composable
fun CommentComposable(navController: NavHostController, userId: Long, text : String, postActivity: PostActivity, commentId: Long) {

    val user = postActivity.getUsernameAndProfilePic(userId)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            CircularImage(navController, convert(user!!.profilePicture), userId)
        }
        Column(
            modifier = Modifier.weight(4f)
        ) {

            Text(text = user!!.username, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.absoluteOffset(y = (-6).dp))
            Text(text = text, fontSize = 12.sp, fontWeight = FontWeight.Light, modifier = Modifier.absoluteOffset(y = (-7).dp), maxLines = 2)
        }
        Column {
            if(userId == getUserId()) {
                IconButton(
                    onClick = {
                        postActivity.deleteComment(commentId)
                        navController.navigateUp()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }
}

@Composable
fun NewComment(postId: Long, userId: Long, navController: NavHostController) {
    val userIdfromJWT = getUserId()
    val context = LocalContext.current
    var commentText by remember {
        mutableStateOf(TextFieldValue(""))
    }
    val path = context.getExternalFilesDir(null)!!.absolutePath
    val tempFile = File(path, "tokens.txt")
    var lines: List<String>? = null
    if(tempFile.isFile) {
        lines = readFileAsLinesUsingUseLines(tempFile)
    }

    val refreshToken = lines?.get(0)
    val accessToken = lines?.get(1)

    val postActivity = PostActivity(accessToken as String, refreshToken as String)

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
                },
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colors.onSurface)
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
                val comment = CommentRequest(post_id = postId, content = commentText.text, commentersId = userIdfromJWT)
                postActivity.comment(comment)
                //navigator.navigate(ViewPostScreenDestination(postId = postId, userId = userId))
                navController.navigate("viewpost/$postId/$userId")
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
    val bmp:Bitmap

    var pic: ByteArray = img!!.toByteArray()
    pic = Base64.decode(pic, Base64.DEFAULT)

    bmp = BitmapFactory.decodeByteArray(pic, 0, pic.size)
    return bmp
}

//@ExperimentalPagerApi
//@Composable
//fun ImagePagerSlider(
//    post: Post,
//    photos: List<Post>
//){
//
//    val pagerState = rememberPagerState(
//        pageCount = photos.size,
//        initialPage = 0
//    )
//
//    Card(
//        modifier = Modifier
//            //.padding(horizontal = 10.dp, vertical = 0.dp)
//            .fillMaxWidth(),
//        shape = RectangleShape,
//        backgroundColor = Color.White
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ){
//
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(250.dp),
//            ){
//
//                HorizontalPager(
//                    state = pagerState,
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(0.dp, 30.dp, 0.dp, 15.dp)
//                ) { page->
//                    Card(
//                        modifier = Modifier
//                            .graphicsLayer {
//                                val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
//
//                                lerp(
//                                    start = 0.85f,
//                                    stop = 1f,
//                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                                ).also { scale ->
//                                    scaleX = scale
//                                    scaleY = scale
//                                }
//
//                                alpha = lerp(
//                                    start = 0.5f,
//                                    stop = 1f,
//                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                                )
//                            }
//                            .fillMaxWidth()
//                            .padding(25.dp, 0.dp, 25.dp, 0.dp),
//                        shape = RoundedCornerShape(15.dp)
//                    ){
//                        val photo = photos[page];
//
//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(Color.LightGray)
//                                .align(Alignment.Center)
//                        )
//
//
//                    }
//                }
//            }
//            HorizontalPagerIndicator(
//                pagerState = pagerState,
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .padding(top = 10.dp, bottom = 15.dp),
//                activeColor = Color.Blue,
//                inactiveColor = Color.LightGray
//            )
//        }
//    }
//}