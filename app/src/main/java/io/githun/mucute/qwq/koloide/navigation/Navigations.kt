package io.githun.mucute.qwq.koloide.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.githun.mucute.qwq.koloide.screen.MainScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavScreen.Main
    ) {

        composable<NavScreen.Main>() {
            MainScreen()
        }

    }
}

@Serializable
sealed interface NavScreen {

    @Serializable
    object Main : NavScreen

}