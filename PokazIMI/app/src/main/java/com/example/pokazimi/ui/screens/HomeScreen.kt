package com.example.pokazimi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pokazimi.ui.composables.Post
import com.example.pokazimi.dataStore.Storage
import com.example.pokazimi.destinations.LoginScreenDestination
import com.example.pokazimi.destinations.MapScreenDestination
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun HomeScreen(navController: NavHostController, navigator: DestinationsNavigator) {
    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colors.background
    SideEffect {
        systemUiController.setStatusBarColor(color)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Button(
            onClick = { navigator.navigate(MapScreenDestination(viewingPost = false)) },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface)
        ) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            Text(
                text = "  Search here",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }

    Column(
        modifier = Modifier
            .absoluteOffset(y = 55.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Post(navController, navigator)
        Post(navController, navigator)
        Post(navController, navigator)
        Spacer(modifier = Modifier.height(115.dp))
    }
}