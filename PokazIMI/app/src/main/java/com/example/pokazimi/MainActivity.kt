package com.example.pokazimi

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.pokazimi.dataStore.Storage
import com.example.pokazimi.destinations.LoginScreenDestination
import androidx.compose.runtime.Composable
import com.example.pokazimi.data.remote.dto.RefreshTokenRequest
import com.example.pokazimi.data.remote.services.AuthService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.example.pokazimi.destinations.MainScreenDestination
import com.example.pokazimi.ui.theme.PokazIMITheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {

    lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            PokazIMITheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}

@com.ramcosta.composedestinations.annotation.Destination(start = true)
@Composable
fun LoadingScreen(navigator: DestinationsNavigator){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = Storage(context)
    val token = dataStore.getAccessToken.collectAsState(initial = "token").value
    val path = context.getExternalFilesDir(null)!!.absolutePath
    val tempFile = File(path, "tokens.txt")
    var lines: List<String>? = null
    if(tempFile.isFile) {
        lines = readFileAsLinesUsingUseLines(tempFile)
    }
    print("linije + " + lines)

    if(token == "") {
        navigator.navigate(LoginScreenDestination)
    }
    else if (token != "token"){
        val authService = AuthService.create()

        val refreshToken = dataStore.getRefreshToken.collectAsState(initial = "token").value
        if(refreshToken != "token") {
            val logInResponse =
                runBlocking { authService.refresh(RefreshTokenRequest(refreshToken as String)) }

            if (logInResponse == null || logInResponse.refreshToken == "Refresh token expired") {

                navigator.navigate(LoginScreenDestination)
            }
            runBlocking {
                if (logInResponse != null) {

                    val path = context.getExternalFilesDir(null)!!.absolutePath
                    val tempFile = File(path, "tokens.txt")
                    tempFile.writeText(logInResponse.refreshToken + "\n" + logInResponse.accessToken)
                    dataStore.saveRefreshToken(logInResponse.refreshToken)
                    dataStore.saveAccessToken(logInResponse.accessToken)
                }
            }
            navigator.navigate(MainScreenDestination)
        }
    }
}



fun readFileAsLinesUsingUseLines(file: File): List<String>
        = file.useLines { it.toList() }


@Composable
fun readFromFile() : List<String>?
{
    val context = LocalContext.current
    val path = context.getExternalFilesDir(null)!!.absolutePath
    val tempFile = File(path, "tokens.txt")
    var lines: List<String>? = null
    if(tempFile.isFile) {
        lines = readFileAsLinesUsingUseLines(tempFile)
    }
    return lines
}
