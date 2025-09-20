package io.github.mucute.qwq.koloide.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class SelectableCardDropDownMenuItem(
    val leadingIcon: ImageVector,
    val textResId: Int
)