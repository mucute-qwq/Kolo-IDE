package io.github.mucute.qwq.koloide.extension.web

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import io.github.mucute.qwq.koloide.extension.ExtensionMain
import io.github.mucute.qwq.koloide.shared.icon.SharedIcons
import io.github.mucute.qwq.koloide.shared.icon.sharedicons.OutlinePackage224

class WebExtensionMain(context: Context) : ExtensionMain(context) {

    private val webExtensionProject by lazy { WebExtensionProject() }

    @Composable
    override fun icon() = rememberVectorPainter(SharedIcons.OutlinePackage224)

    override fun extensionProject() = webExtensionProject

}