package com.example.pokazimi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pokazimi.ui.theme.PokazIMITheme
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokazIMITheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}
