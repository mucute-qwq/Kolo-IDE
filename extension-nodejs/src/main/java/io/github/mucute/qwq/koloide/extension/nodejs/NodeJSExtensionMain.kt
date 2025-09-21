package io.github.mucute.qwq.koloide.extension.nodejs

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import io.github.mucute.qwq.koloide.extension.ExtensionMain

class NodeJSExtensionMain(context: Context) : ExtensionMain(context) {

    private val nodeJSExtensionProject by lazy { NodeJSExtensionProject() }

    @Composable
    override fun icon() = painterResource(R.drawable.outline_package_2_24)

    override fun extensionProject() = nodeJSExtensionProject

}