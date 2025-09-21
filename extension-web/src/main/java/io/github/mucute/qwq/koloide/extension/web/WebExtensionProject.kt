package io.github.mucute.qwq.koloide.extension.web

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import io.github.mucute.qwq.koloide.extension.ExtensionProject
import io.github.mucute.qwq.koloide.shared.icon.SharedIcons
import io.github.mucute.qwq.koloide.shared.icon.sharedicons.Html24

class WebExtensionProject : ExtensionProject {

    @Composable
    override fun newProjectIcon() = rememberVectorPainter(SharedIcons.Html24)

}