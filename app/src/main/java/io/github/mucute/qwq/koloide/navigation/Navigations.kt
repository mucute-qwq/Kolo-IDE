package io.github.mucute.qwq.koloide.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.mucute.qwq.koloide.composition.provider.LocalNavController
import io.github.mucute.qwq.koloide.screen.MainScreen
import io.github.mucute.qwq.koloide.screen.NewProjectOptionsScreen
import io.github.mucute.qwq.koloide.screen.NewProjectScreen
import io.github.mucute.qwq.koloide.screen.TerminalScreen
import io.github.mucute.qwq.koloide.screen.WorkspaceScreen
import kotlinx.serialization.Serializable
import me.zhanghai.compose.preference.ProvidePreferenceLocals

@Composable
fun Navigation() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        ProvidePreferenceLocals {
            NavHost(
                navController = navController,
                startDestination = NavScreen.Main
            ) {

                composable<NavScreen.Main> {
                    MainScreen()
                }

                composable<NavScreen.NewProject> {
                    NewProjectScreen()
                }

                composable<NavScreen.NewProjectOptions> {
                    NewProjectOptionsScreen(it.toRoute<NavScreen.NewProjectOptions>().module)
                }

                composable<NavScreen.Workspace> {
                    WorkspaceScreen()
                }

                composable<NavScreen.Terminal> {
                    TerminalScreen()
                }

            }
        }
    }
}

@Serializable
sealed interface NavScreen {

    @Serializable
    object Main : NavScreen

    @Serializable
    object NewProject : NavScreen

    @Serializable
    data class NewProjectOptions(val module: String) : NavScreen

    @Serializable
    object Workspace : NavScreen

    @Serializable
    object Terminal : NavScreen

}