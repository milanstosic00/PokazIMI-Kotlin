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
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import com.example.pokazimi.data.remote.model.UsernameAndProfilePic
import com.example.pokazimi.ui.activity.PostActivity
import com.example.pokazimi.ui.composables.CircularImage
import com.example.pokazimi.ui.screens.convert
import com.google.accompanist.pager.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import java.io.File
import java.io.FileOutputStream
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.P)
@Destination
@Composable
fun PostScreen(navController: NavHostController, navigator: DestinationsNavigator) {
    if(navController.previousBackStackEntry?.destination == navController.currentBackStackEntry?.destination) {
        navController.popBackStack()
    }

    val context = LocalContext.current

    var desciption by remember {
        mutableStateOf(TextFieldValue(""))
    }

    val slika: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), android.R.mipmap.sym_def_app_icon)

    val slike = remember {
        mutableStateListOf(slika, slika, slika, slika, slika)
    }

    val chooseImage1 = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
        val source = ImageDecoder.createSource(context.contentResolver, it as Uri)
        slike[0] = ImageDecoder.decodeBitmap(source)
    }
    val chooseImage2 = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
        val source = ImageDecoder.createSource(context.contentResolver, it as Uri)
        slike[1] = ImageDecoder.decodeBitmap(source)
    }
    val chooseImage3 = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
        val source = ImageDecoder.createSource(context.contentResolver, it as Uri)
        slike[2] = ImageDecoder.decodeBitmap(source)
    }
    val chooseImage4 = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
        val source = ImageDecoder.createSource(context.contentResolver, it as Uri)
        slike[3] = ImageDecoder.decodeBitmap(source)
    }
    val chooseImage5 = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
        val source = ImageDecoder.createSource(context.contentResolver, it as Uri)
        slike[4] = ImageDecoder.decodeBitmap(source)
    }

    val pagerState = rememberPagerState(
        pageCount = 5,
        initialPage = 0
    )

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

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        PreviewHeader(navController, desciption.text, slike)
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            BasicTextField(
                value = desciption,
                onValueChange = {desciption = it},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .border(
                        BorderStroke(1.dp, MaterialTheme.colors.onSurface),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp),
                decorationBox = { innerTextField ->
                    Row(

                    ) {
                        if(desciption.text.isEmpty()) {
                            Text("Description...")
                        }
                        innerTextField()
                    }
                },
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colors.onSurface)
            )
        }

        Column(
            modifier = Modifier
                .height(530.dp)
                .fillMaxWidth()
        ) {

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp, 10.dp, 0.dp, 10.dp)
            ) { page ->
                Card(
                    modifier = Modifier
                        .graphicsLayer {
                            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                            lerp(
                                start = 0.85f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }
                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }
                        .fillMaxWidth()
                        .height(500.dp)
                        .padding(horizontal = 10.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),

                        ) {
                        when (page) {
                            0 -> {Image(bitmap = slike[0].asImageBitmap(), contentDescription = "Image", modifier = Modifier.fillMaxSize()); Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) { Button(onClick = { chooseImage1.launch("image/*") }, modifier = Modifier.height(50.dp).width(160.dp), shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 4.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black, contentColor = Color.White)) { Text(text = "Choose image") }}}
                            1 -> {Image(bitmap = slike[1].asImageBitmap(), contentDescription = "Image", modifier = Modifier.fillMaxSize()); Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) { Button(onClick = { chooseImage2.launch("image/*") }, modifier = Modifier.height(50.dp).width(160.dp), shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 4.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black, contentColor = Color.White)) { Text(text = "Choose image") }}}
                            2 -> {Image(bitmap = slike[2].asImageBitmap(), contentDescription = "Image", modifier = Modifier.fillMaxSize()); Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) { Button(onClick = { chooseImage3.launch("image/*") }, modifier = Modifier.height(50.dp).width(160.dp), shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 4.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black, contentColor = Color.White)) { Text(text = "Choose image") }}}
                            3 -> {Image(bitmap = slike[3].asImageBitmap(), contentDescription = "Image", modifier = Modifier.fillMaxSize()); Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) { Button(onClick = { chooseImage4.launch("image/*") }, modifier = Modifier.height(50.dp).width(160.dp), shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 4.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black, contentColor = Color.White)) { Text(text = "Choose image") }}}
                            4 -> {Image(bitmap = slike[4].asImageBitmap(), contentDescription = "Image", modifier = Modifier.fillMaxSize()); Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) { Button(onClick = { chooseImage5.launch("image/*") }, modifier = Modifier.height(50.dp).width(160.dp), shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 4.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black, contentColor = Color.White)) { Text(text = "Choose image") }}}
                        }
                    }
                }
            }
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        }

    }
}

@Composable
fun PreviewHeader(navController: NavHostController, description: String, images: MutableList<Bitmap>) {
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
                    val images2 = mutableListOf<Bitmap>()
                    images.forEach {
                        if (!it.sameAs(BitmapFactory.decodeResource(Resources.getSystem(), android.R.mipmap.sym_def_app_icon))) {
                            images2.add(it)
                            println("DODAO SAM")
                        }
                    }

                    var dec = description
                    if(dec == "") {
                        dec = "No description"
                    }

                    images2.forEachIndexed { index, element ->
                        val path = context.getExternalFilesDir(null)!!.absolutePath
                        val tempFile = File(path, "tempFileName$index.jpg")
                        val fOut = FileOutputStream(tempFile)
                        element.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                        fOut.close()
                    }
                    navController.navigate("map/true/false/$dec")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Forward",
                    Modifier.size(30.dp),
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}

