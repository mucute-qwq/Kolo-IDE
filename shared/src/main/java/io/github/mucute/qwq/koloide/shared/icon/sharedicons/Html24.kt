package io.github.mucute.qwq.koloide.shared.icon.sharedicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.mucute.qwq.koloide.shared.icon.SharedIcons

public val SharedIcons.Html24: ImageVector
    get() {
        if (_html24 != null) {
            return _html24!!
        }
        _html24 = Builder(name = "Html24", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 640.0f, viewportHeight = 640.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(128.0f, 96.0f)
                lineTo(162.9f, 491.8f)
                lineTo(319.5f, 544.0f)
                lineTo(477.1f, 491.8f)
                lineTo(512.0f, 96.0f)
                lineTo(128.0f, 96.0f)
                close()
                moveTo(436.2f, 223.9f)
                lineTo(252.4f, 223.9f)
                lineTo(256.5f, 273.3f)
                lineTo(432.1f, 273.3f)
                lineTo(418.5f, 421.7f)
                lineTo(320.6f, 448.7f)
                lineTo(320.6f, 449.0f)
                lineTo(319.5f, 449.0f)
                lineTo(220.8f, 421.7f)
                lineTo(214.8f, 345.9f)
                lineTo(262.5f, 345.9f)
                lineTo(266.0f, 384.0f)
                lineTo(319.5f, 398.5f)
                lineTo(373.2f, 384.0f)
                lineTo(379.2f, 321.8f)
                lineTo(212.3f, 321.8f)
                lineTo(199.5f, 176.2f)
                lineTo(440.6f, 176.2f)
                lineTo(436.2f, 223.9f)
                close()
            }
        }
        .build()
        return _html24!!
    }

private var _html24: ImageVector? = null
