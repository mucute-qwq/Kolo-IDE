package io.github.mucute.qwq.koloide.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class NavItem(
    val icon: ImageVector,
    val labelResId: Int,
    val content: @Composable () -> Unit
)