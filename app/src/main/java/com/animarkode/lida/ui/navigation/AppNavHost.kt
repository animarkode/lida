package com.animarkode.lida.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.animarkode.lida.feature.edit.EditScreen
import com.animarkode.lida.feature.list.ListScreen

private const val LIST = "list"
private const val EDIT = "edit?id={id}"

/**
 * Runs [action] only while this destination is the resumed one, so repeated taps
 * during a transition can't pop the start destination or push duplicate screens.
 */
private inline fun NavBackStackEntry.ifResumed(action: () -> Unit) {
    if (lifecycle.currentState == Lifecycle.State.RESUMED) action()
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = LIST) {
        composable(LIST) { entry ->
            ListScreen(
                onAdd = { entry.ifResumed { navController.navigate("edit") } },
                onOpen = { id -> entry.ifResumed { navController.navigate("edit?id=$id") } },
            )
        }
        composable(
            route = EDIT,
            arguments = listOf(navArgument("id") { type = NavType.StringType; nullable = true; defaultValue = null }),
        ) { entry ->
            EditScreen(onDone = { entry.ifResumed { navController.popBackStack() } })
        }
    }
}
