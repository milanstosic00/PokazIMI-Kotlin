package com.example.pokazimi

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pokazimi.data.item.BottomNavItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


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

@Composable
fun Navigation(navController: NavHostController, navigator: DestinationsNavigator) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("post") {
            PostScreen(navController)
        }
        composable("profile") {
            ProfileScreen(0, navigator, navController) // 0 znaci da gleda svoj profil - u navbaru ostaje hardkodovana 0
        }
    }
}

@Composable
fun BottomBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
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
