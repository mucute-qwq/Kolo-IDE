package io.github.mucute.qwq.koloide.extension

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

abstract class ExtensionMain(
    val context: Context
) {

    @Composable
    abstract fun icon(): Painter

    abstract fun extensionProject(): ExtensionProject?

}