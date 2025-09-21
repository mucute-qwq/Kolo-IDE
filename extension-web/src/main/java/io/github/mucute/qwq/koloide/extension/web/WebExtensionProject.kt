package io.github.mucute.qwq.koloide.extension.web

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import io.github.mucute.qwq.koloide.extension.ExtensionProject

class WebExtensionProject : ExtensionProject {

    @Composable
    override fun newProjectIcon() = painterResource(R.drawable.html_24)

}