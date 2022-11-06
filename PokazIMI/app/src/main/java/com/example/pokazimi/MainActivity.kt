package com.example.pokazimi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.pokazimi.dataStore.Storage
import com.example.pokazimi.destinations.LoginScreenDestination
import androidx.compose.runtime.Composable
import com.example.pokazimi.data.remote.dto.RefreshTokenRequest
import com.example.pokazimi.data.remote.services.AuthService
import com.example.pokazimi.destinations.MainScreenDestination
import com.example.pokazimi.ui.theme.PokazIMITheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            /*
            val posts = produceState<List<PostResponse>>(
                initialValue = emptyList(),
                producer = {
                    value = service.getPosts()
                }
            )
            */
            PokazIMITheme {
                DestinationsNavHost(navGraph = NavGraphs.root)

                /*
                Surface(color = MaterialTheme.colors.background) {
                    LazyColumn {
                        items(posts.value) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(text = it.title, fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = it.body, fontSize = 14.sp)
                            }
                        }
                    }
                }
                */
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


    if(token == "") {
        navigator.navigate(LoginScreenDestination)
    }
    else if (token != "token"){
        val authService = AuthService.create()

        val refreshToken = dataStore.getRefreshToken.collectAsState(initial = "token").value
        if(refreshToken != "token") {
            val logInResponse =
                runBlocking { authService.refresh(RefreshTokenRequest(refreshToken as String)) }

            if (logInResponse == null || logInResponse.accessToken == "Refresh token expired") {

                navigator.navigate(LoginScreenDestination)
            }
            runBlocking {
                if (logInResponse != null) {
                    dataStore.saveRefreshToken(logInResponse.refreshToken)
                    dataStore.saveAccessToken(logInResponse.accessToken)
                }
            }
            navigator.navigate(MainScreenDestination)
        }
    }
}


