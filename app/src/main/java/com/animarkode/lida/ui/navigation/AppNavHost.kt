package com.animarkode.lida.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.animarkode.lida.feature.edit.EditScreen
import com.animarkode.lida.feature.list.ListScreen

private const val LIST = "list"
private const val EDIT = "edit?id={id}"

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = LIST) {
        composable(LIST) {
            ListScreen(
                onAdd = { navController.navigate("edit") },
                onOpen = { id -> navController.navigate("edit?id=$id") },
            )
        }
        composable(
            route = EDIT,
            arguments = listOf(navArgument("id") { type = NavType.StringType; nullable = true; defaultValue = null }),
        ) {
            EditScreen(onDone = { navController.popBackStack() })
        }
    }
}
