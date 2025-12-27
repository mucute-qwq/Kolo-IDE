package io.github.mucute.qwq.koloide.module.nodejs

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import io.github.mucute.qwq.koloide.module.Module

class NodeJSModule(application: Application) : Module(
    application = application,
    titleResId = R.string.module_title,
    subtitleResId = R.string.module_subtitle
) {
    override val module = "nodejs"

    @Composable
    override fun newProjectIcon(): Painter {
        return painterResource(R.drawable.node_js_brands_solid_full)
    }

}