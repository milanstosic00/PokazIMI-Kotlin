package com.example.pokazimi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokazimi.data.remote.RequestService
import com.example.pokazimi.data.remote.dto.PostResponse
import com.example.pokazimi.destinations.Destination
import com.example.pokazimi.destinations.HomeScreenDestination
import com.example.pokazimi.destinations.MainScreenDestination
import com.example.pokazimi.ui.theme.PokazIMITheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class MainActivity : ComponentActivity() {

    //private val service = RequestService.create()

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
fun Init(navigator: DestinationsNavigator){
    // AKO JE ULOGOVAN REDIREKT NA MAIN SCREEN, A AKO NIJE ONDA NA LOGIN SCREEN
    navigator.navigate(MainScreenDestination)
}
