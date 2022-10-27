package com.example.pokazimi

import android.media.Image
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.pokazimi.destinations.DirectionDestination
import com.example.pokazimi.destinations.HomeScreenDestination
import com.example.pokazimi.destinations.PostScreenDestination
import com.example.pokazimi.destinations.ProfileScreenDestination

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)