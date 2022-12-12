package com.example.pokazimi

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokazimi.data.item.BottomNavItem
import com.example.pokazimi.ui.screens.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@RequiresApi(Build.VERSION_CODES.P)
@Destination
@Composable
fun MainScreen(navigator: DestinationsNavigator) {
    val navController = rememberNavController()

    Scaffold (
        bottomBar = {
            BottomBar(
                items = listOf(
                    BottomNavItem(
                        name = "Home",
                        route = "home",
                        icon = Icons.Default.Home
                    ),
                    BottomNavItem(
                        name = "Post",
                        route = "post",
                        icon = Icons.Default.Add
                    ),
                    BottomNavItem(
                        name = "Profile",
                        route = "profile",
                        icon = Icons.Default.Person
                    )
                ),
                navController = navController,
                onItemClick = {
                    navController.navigate(it.route)
                }
            )
        }
    ) {
        Navigation(navController = navController, navigator)
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun Navigation(navController: NavHostController, navigator: DestinationsNavigator) {
    val userIdfromJWT = getUserId()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, navigator)
        }
        composable("home/1") {
            HomeScreen(navController, navigator, 1)
        }
        composable("search/{radius}/{latitude}/{longitude}", arguments = listOf(navArgument(name = "radius"){ type = NavType.FloatType},navArgument(name = "latitude"){ type = NavType.FloatType},navArgument(name = "longitude"){ type = NavType.FloatType})) {
            SearchResultScreen(navController, navigator, radius = it.arguments!!.getFloat("radius").toDouble(), latitude = it.arguments!!.getFloat("latitude").toDouble(), longitude = it.arguments!!.getFloat("longitude").toDouble())
        }
        composable("post") {
            PostScreen(navController, navigator)
        }
        composable("profile") {
            ProfileScreen(userIdfromJWT, navigator, navController)
        }
        composable(route = "profile/{userId}", arguments = listOf(navArgument(name = "userId"){ type = NavType.LongType })) {
            ProfileScreen(userId = it.arguments!!.getLong("userId"), navigator, navController)
        }
        composable("viewpost/{postId}/{userId}", arguments = listOf(navArgument(name = "postId"){ type = NavType.LongType }, navArgument(name = "userId"){ type = NavType.LongType })) {
            ViewPostScreen(navController, navigator, postId = it.arguments!!.getLong("postId"), userId = it.arguments!!.getLong("userId"))
        }
        composable("map/{viewingPost}", arguments = listOf(navArgument(name = "viewingPost"){ type = NavType.BoolType })) {
            MapScreen(navController = navController, viewingPost = it.arguments!!.getBoolean("viewingPost"), description = "")
        }
        composable("map/{newPost}/{viewingPost}/{description}", arguments = listOf(navArgument(name = "newPost"){ type = NavType.BoolType }, navArgument(name = "description"){ type = NavType.StringType }, navArgument(name = "viewingPost"){ type = NavType.BoolType })) {
            MapScreen(navController = navController, newPost = it.arguments!!.getBoolean("newPost"), viewingPost = it.arguments!!.getBoolean("viewingPost"), description = it.arguments!!.getString("description"))
        }
        composable("map/{newPost}/{viewingPost}/{description}/{longitude}/{latitude}", arguments = listOf(navArgument(name = "newPost"){ type = NavType.BoolType }, navArgument(name = "description"){ type = NavType.StringType }, navArgument(name = "viewingPost"){ type = NavType.BoolType }, navArgument(name = "longitude"){ type = NavType.FloatType }, navArgument(name = "latitude"){ type = NavType.FloatType })) {
            MapScreen(navController = navController, newPost = it.arguments!!.getBoolean("newPost"), viewingPost = it.arguments!!.getBoolean("viewingPost"), description = it.arguments!!.getString("description"), longitude = it.arguments!!.getFloat("longitude"), latitude = it.arguments!!.getFloat("latitude"))
        }
        composable("search") {
            SearchLocationScreen(navController = navController)
        }
    }

}

@Composable
fun BottomBar(
    items: List<BottomNavItem>,
    navController: NavController,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation (
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp
    ){
        items.forEach { destination ->
            val selected = destination.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(destination) },
                selectedContentColor = MaterialTheme.colors.onSurface,
                icon = {
                    Icon(destination.icon, contentDescription = destination.name)
                }
            )
        }
    }
}
