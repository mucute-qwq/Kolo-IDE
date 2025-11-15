package io.github.mucute.qwq.koloide.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.mucute.qwq.koloide.screen.LspTestScreen
import io.github.mucute.qwq.koloide.screen.MainScreen
import io.github.mucute.qwq.koloide.screen.NewProjectScreen
import io.github.mucute.qwq.koloide.screen.TerminalScreen
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

                composable<NavScreen.Terminal> {
                    TerminalScreen()
                }

                composable<NavScreen.LspTest> {
                    LspTestScreen()
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
    object Terminal : NavScreen

    @Serializable
    object LspTest : NavScreen

}

val LocalNavController =
    compositionLocalOf<NavController> { noLocalProvidedFor("LocalNavController") }

@Suppress("SameParameterValue")
private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}