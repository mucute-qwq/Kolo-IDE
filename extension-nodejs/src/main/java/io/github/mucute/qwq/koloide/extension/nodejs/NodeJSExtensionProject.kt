package io.github.mucute.qwq.koloide.extension.nodejs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import io.github.mucute.qwq.koloide.extension.ExtensionProject

class NodeJSExtensionProject : ExtensionProject {

    @Composable
    override fun newProjectIcon() = painterResource(R.drawable.nodejs_24)

}