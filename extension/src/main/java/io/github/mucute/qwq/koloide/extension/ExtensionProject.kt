package io.github.mucute.qwq.koloide.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

interface ExtensionProject {

    @Composable
    fun newProjectIcon(): Painter

}