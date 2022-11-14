package com.example.pokazimi.ui.screens

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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pokazimi.data.remote.dto.ChangeProfilePictureRequest
import com.example.pokazimi.data.remote.services.ChangeProfilePictureService
import com.example.pokazimi.ui.composables.Post
import com.example.pokazimi.dataStore.Storage
import com.example.pokazimi.destinations.LoginScreenDestination
import com.example.pokazimi.ui.activity.ProfileAcivity
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream

@Destination
@Composable
fun ProfileScreen(userId: Int, navigator: DestinationsNavigator, navController: NavHostController) {

    val following by remember {
        mutableStateOf(false)
    }

    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colors.background
    SideEffect {
        systemUiController.setStatusBarColor(color)
    }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        ProfileInfo(userId, navigator, following, navController)
        Spacer(modifier = Modifier.height(20.dp))
        ProfileStats()
        Spacer(modifier = Modifier.height(10.dp))
        Divide()
        Post(navController, navigator)
        Post(navController, navigator)
        Spacer(modifier = Modifier.height(65.dp))
    }
}

@Composable
fun ProfileInfo(userId: Int, navigator: DestinationsNavigator, following: Boolean, navController: NavHostController) {

    val client = ChangeProfilePictureService.create()
    val context = LocalContext.current
    val myImage: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), android.R.mipmap.sym_def_app_icon)
    val result = remember {
        mutableStateOf<Bitmap>(myImage)
    }

    val profileAcivity = ProfileAcivity()

    val chooseImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
        if (Build.VERSION.SDK_INT < 29) {
            result.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
        }
        else {
            val source = ImageDecoder.createSource(context.contentResolver, it as Uri)
            result.value = ImageDecoder.decodeBitmap(source)
        }
        profileAcivity.changeProfilePicture(1, result.value)
    }

    val scope = rememberCoroutineScope()
    val dataStore = Storage(context)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { navController.navigate("home") }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.size(30.dp))
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    result.value.asImageBitmap(),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .width(120.dp)
                        .height(120.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.onSurface,
                            shape = CircleShape
                        )
                        .padding(3.dp)
                        .clip(CircleShape),
                )
                Button(
                    onClick = {
                        chooseImage.launch("image/*")

                    },
                    shape = CircleShape,
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.onSurface,
                            shape = CircleShape
                        )
                        .align(Alignment.BottomEnd),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoCamera,
                        contentDescription = "Change profile picture",
                        modifier = Modifier
                            .clip(CircleShape)
                            .padding(PaddingValues(0.dp)),
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Ako gleda tudji profil onda se vidi follow dugme
            //Ako gleda svoj profil onda se vidi logout dugme

            if(userId == 0) {
                IconButton(onClick = {
                    scope.launch { dataStore.saveAccessToken("") }
                    navigator.navigate(LoginScreenDestination) }
                ) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout", modifier = Modifier.size(30.dp))
                }
            }
            else {
                IconButton(onClick = { !following }) {
                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = "Follow",
                        modifier = Modifier.size(30.dp),
                        tint = isFollowing(following)
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Dugoime Dugoprezime",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "@username",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ProfileStats() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        ProfileStat(numberText = "108", text = "Posts", Modifier.weight(1f))
        ProfileStat(numberText = "3,820", text = "Followers", Modifier.weight(1f))
        ProfileStat(numberText = "6,290", text = "Following", Modifier.weight(1f))
    }
}

@Composable
fun ProfileStat(
    numberText: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = numberText,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            text = text,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun isFollowing(following: Boolean): Color {
    if(following) {
        return Color.Green
    }
    return MaterialTheme.colors.onSurface
}

@Composable
fun Divide() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .padding(horizontal = 10.dp)
    ) {
        Divider(color = MaterialTheme.colors.onSurface, thickness = 1.dp)
    }
}


