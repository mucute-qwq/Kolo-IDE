package io.github.mucute.qwq.koloide.extension.nodejs

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import io.github.mucute.qwq.koloide.extension.ExtensionMain
import io.github.mucute.qwq.koloide.shared.icon.SharedIcons
import io.github.mucute.qwq.koloide.shared.icon.sharedicons.OutlinePackage224

class NodeJSExtensionMain(context: Context) : ExtensionMain(context) {

    private val nodeJSExtensionProject by lazy { NodeJSExtensionProject() }

    @Composable
    override fun icon() = rememberVectorPainter(SharedIcons.OutlinePackage224)

    override fun extensionProject() = nodeJSExtensionProject

}