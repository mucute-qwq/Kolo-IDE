package io.github.mucute.qwq.koloide.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.platform.LocalContext
import io.github.mucute.qwq.koloide.extension.Extension

@OptIn(InternalComposeApi::class)
@Composable
fun <T> Extension.useExtensionContext(block: @Composable (extension: Extension) -> T): T {
    currentComposer.startProvider(LocalContext provides extensionMain.context)
    val result = block(this)
    currentComposer.endProvider()
    return result
}