package io.github.mucute.qwq.koloide.screen

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import cn.mucute.merminal.composable.Terminal
import cn.mucute.merminal.composable.rememberSessionController
import io.github.mucute.qwq.koloide.navigation.LocalNavController

@Composable
fun TerminalScreen() {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val sessionController = rememberSessionController(
        currentWorkingDirectory = context.filesDir.absolutePath,
        environment = cn.mucute.merminal.composable.systemEnvironment().apply {
            put("HOME", context.filesDir.absolutePath)
            put("PATH", "${System.getenv("PATH")}:${context.filesDir.resolve("usr/bin")}")
        },
        command = null
    )
    Surface {
        Terminal(
            sessionController = sessionController,
            onSessionFinished = {
                navController.navigateUp()
            }
        )
    }
}