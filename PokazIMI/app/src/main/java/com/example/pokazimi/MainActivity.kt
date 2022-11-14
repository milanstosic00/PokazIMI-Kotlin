package com.example.pokazimi

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.pokazimi.dataStore.Storage
import com.example.pokazimi.destinations.LoginScreenDestination
import androidx.compose.runtime.Composable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.example.pokazimi.destinations.MainScreenDestination
import com.example.pokazimi.ui.theme.PokazIMITheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    navigator.navigate(MainScreenDestination)
    val context = LocalContext.current
    //val scope = rememberCoroutineScope()
    val dataStore = Storage(context)
    val token = dataStore.getAccessToken.collectAsState(initial = "token").value

    if(token == "") {
        navigator.navigate(LoginScreenDestination)
    }
    else if (token != "token"){
        navigator.navigate(MainScreenDestination)
    }
}


