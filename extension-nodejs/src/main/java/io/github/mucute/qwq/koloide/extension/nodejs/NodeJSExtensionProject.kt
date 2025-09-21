package io.github.mucute.qwq.koloide.extension.nodejs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import io.github.mucute.qwq.koloide.extension.ExtensionProject
import io.github.mucute.qwq.koloide.shared.icon.SharedIcons
import io.github.mucute.qwq.koloide.shared.icon.sharedicons.Nodejs24

class NodeJSExtensionProject : ExtensionProject {

    @Composable
    override fun newProjectIcon() = rememberVectorPainter(SharedIcons.Nodejs24)

}