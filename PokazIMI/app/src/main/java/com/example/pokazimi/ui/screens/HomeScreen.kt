package com.example.pokazimi

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pokazimi.dataStore.Storage
import com.example.pokazimi.destinations.LoginScreenDestination
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = Storage(context)

    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colors.background
    SideEffect {
        systemUiController.setStatusBarColor(color)
    }

    Scaffold() {
        Text(text = "Home Screen!")
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                scope.launch { dataStore.saveAccessToken("") }
                navigator.navigate(LoginScreenDestination)
            }
        ) {
            Text(text = "LOG OUT")
        }
    }
    

}