package io.github.mucute.qwq.koloide.module

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.twotone.Code
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource

abstract class Module(
    val icon: @Composable () -> Painter = { painterResource(R.drawable.package_2_24px) },
    val titleResId: Int,
    val subtitleResId: Int,
) {

    abstract val name: String

    @Composable
    open fun newProjectIcon(): Painter {
        return rememberVectorPainter(Icons.TwoTone.Code)
    }

}