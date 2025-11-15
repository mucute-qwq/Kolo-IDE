package io.github.mucute.qwq.koloide.screen

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cn.mucute.merminal.composable.Terminal
import cn.mucute.merminal.composable.rememberSessionController
import cn.mucute.merminal.composable.systemEnvironment

@Composable
fun TerminalScreen() {
    val context = LocalContext.current
    val sessionController = rememberSessionController(
        currentWorkingDirectory = context.filesDir.absolutePath,
        environment = systemEnvironment().apply {
            put("HOME", context.filesDir.absolutePath)
        }
    )
    Terminal(
        sessionController = sessionController,
        modifier = Modifier.systemBarsPadding()
    )
}