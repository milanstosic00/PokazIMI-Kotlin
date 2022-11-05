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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pokazimi.ui.activity.PostActivity
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun PostScreen(navController: NavHostController) {

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

    var text by remember {
        mutableStateOf("Write you description here")
    }

    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colors.background
    SideEffect {
        systemUiController.setStatusBarColor(color)
    }
//
    val postActivity = PostActivity()
    // treba da se dodaju slika i opis, a automatski se dodaju, username, datum, vreme, lajkovi = 0, ocene = prazno i komentari = prazno

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Column (
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "New Post",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(5.dp)
                )
            }
            Column (
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = {
                         postActivity.savePost(1,text,result.value)
                    },
                    modifier = Modifier
                        .padding(5.dp)
                ) {
                    Icon(
                        Icons.Default.Done,
                        contentDescription = "Post",
                        modifier = Modifier
                            .size(42.dp)
                    )
                }
            }
        }


        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(470.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .height(460.dp)
                    .width(360.dp),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 5.dp
            ) {
                Column(

                ) {
                    Row() {
                        Image(
                            result.value.asImageBitmap(),
                            //painter = painterResource(id = R.drawable.test_img),
                            contentDescription = "Image",
                            modifier = Modifier
                                .height(360.dp)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.test_img),
                                    contentDescription = "Image",
                                    modifier = Modifier
                                        .height(48.dp)
                                        .width(48.dp)
                                        .padding(4.dp)
                                        .clip(CircleShape)
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(3f),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(text = "@Username", fontSize = 12.sp, modifier = Modifier.padding(3.dp), fontWeight = FontWeight.Bold)
                                Text(text = "Lokacija, Neko Mesto", fontSize = 10.sp, modifier = Modifier.padding(start = 3.dp))
                            }
                            Column (
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "Like", modifier = Modifier.size(34.dp))
                            }
                            Column (
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(6.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Icon(imageVector = Icons.Default.ChatBubbleOutline, contentDescription = "Like", modifier = Modifier.size(32.dp))
                            }
                            Column (
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Icon(imageVector = Icons.Default.StarOutline, contentDescription = "Like", modifier = Modifier.size(34.dp))
                            }
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        BasicTextField(
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxSize()
                                .background(MaterialTheme.colors.background),
                            value = text,
                            onValueChange = {newText -> text = newText}
                        )
                    }

                }
            }
        }
        Row() {
            Column (
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { chooseImage.launch("image/*") },
                    modifier = Modifier
                        .width(160.dp),
                    contentPadding = PaddingValues(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 4.dp)
                ) {
                    Text(text = "Choose Image")
                }
            }
            Column (
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {  },
                    modifier = Modifier
                        .width(160.dp),
                    contentPadding = PaddingValues(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 4.dp)
                ) {
                    Text(text = "Choose Location")
                }
            }
        }
    }
}


