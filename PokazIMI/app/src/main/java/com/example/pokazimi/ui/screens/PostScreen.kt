package com.example.pokazimi

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.example.pokazimi.data.remote.dto.UsernameAndProfilePic
import com.example.pokazimi.destinations.MapScreenDestination
import com.example.pokazimi.ui.activity.PostActivity
import com.example.pokazimi.ui.composables.CircularImage
import com.example.pokazimi.ui.composables.PostContent
import com.example.pokazimi.ui.composables.PostFooter
import com.example.pokazimi.ui.composables.PostHeader
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream

@Destination
@Composable
fun PostScreen(navController: NavHostController, navigator: DestinationsNavigator) {

    val context = LocalContext.current
    val myImage: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), android.R.mipmap.sym_def_app_icon)
    val result = remember {
        mutableStateOf<Bitmap>(myImage)
    }

    val chooseImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
        if (Build.VERSION.SDK_INT < 29) {
            result.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
        }
        else {
            val source = ImageDecoder.createSource(context.contentResolver, it as Uri)
            result.value = ImageDecoder.decodeBitmap(source)
        }
    }


    var desciption by remember {
        mutableStateOf(TextFieldValue(""))
    }

    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colors.background
    SideEffect {
        systemUiController.setStatusBarColor(color)
    }
//
    val postActivity = PostActivity()
    // treba da se dodaju slika i opis, a automatski se dodaju, username, datum, vreme, lajkovi = 0, ocene = prazno i komentari = prazno

    val usernameAndProfilePic = runBlocking { postActivity.getUsernameAndProfilePic(1) }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        PreviewHeader(navController, navigator, desciption.text, result.value)

        PostPreview(navController, navigator, result.value, desciption.text, usernameAndProfilePic)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(10.dp, 10.dp, 10.dp, 10.dp))
        ) {
            Text(text = "Write your comment here", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            BasicTextField(
                value = desciption,
                onValueChange = {desciption = it},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .border(
                    BorderStroke(1.dp, MaterialTheme.colors.onSurface),
                    shape = RoundedCornerShape(8.dp)
                ),
                decorationBox = { innerTextField ->
                    Row(

                    ) {
                        if(desciption.text.isEmpty()) {
                            Text("Description...")
                        }
                        innerTextField()
                    }
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .absoluteOffset(y = (-10).dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { chooseImage.launch("image/*") },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 4.dp)
            ) {
                Text(text = "Tap here to choose image")
            }
        }
    }
}

@Composable
fun PreviewHeader(navController: NavHostController, navigator: DestinationsNavigator, description: String, image: Bitmap) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(PaddingValues(0.dp, 10.dp, 10.dp, 10.dp))
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            IconButton(
                onClick = { navController.navigateUp() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    Modifier.size(30.dp),
                    tint = MaterialTheme.colors.onSurface
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
                text = "New Post",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                modifier = Modifier.absoluteOffset(y = (-2).dp)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(
                onClick = {
                    val path = context.getExternalFilesDir(null)!!.absolutePath
                    val tempFile = File(path, "tempFileName.jpg")
                    val fOut = FileOutputStream(tempFile)
                    image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                    fOut.close()
                    navigator.navigate(MapScreenDestination(newPost = true, viewingPost = false, description = description))
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Back",
                    Modifier.size(30.dp),
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}

@Composable
fun PostPreviewHeader(desciption: String, usernameAndProfilePic: UsernameAndProfilePic?) {

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
            //CircularImage()
        }

        Column(
            modifier = Modifier
                .weight(5f)
                .padding(5.dp)
        ) {
            if (usernameAndProfilePic != null) {
                Text(text = usernameAndProfilePic.username, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            Text(text = "1 hour ago", fontSize = 10.sp, fontWeight = FontWeight.Light)
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .padding(start = 10.dp)
    ) {
        Text(text = desciption, fontSize = 14.sp)
    }
}

@Composable
fun PostPreview(navController: NavHostController, navigator: DestinationsNavigator, image: Bitmap, desciption: String, usernameAndProfilePic: UsernameAndProfilePic?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 5.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                PostPreviewHeader(desciption, usernameAndProfilePic)
                Spacer(modifier = Modifier.height(10.dp))
                PostImagePreview(image)
                //PostFooter(navController, navigator)
            }
        }
    }
}

@Composable
fun PostImagePreview(image: Bitmap) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(330.dp)
            .padding(horizontal = 10.dp)
    ) {
        Image(
            image.asImageBitmap(),
            contentDescription = "Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
                .clip(shape = RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
    }
}


