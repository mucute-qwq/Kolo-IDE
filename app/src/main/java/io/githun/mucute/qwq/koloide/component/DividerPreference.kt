package io.githun.mucute.qwq.koloide.component

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable

fun LazyListScope.dividerPreference() {
    item {
        DividerPreference()
    }
}

@Composable
fun DividerPreference() {
    HorizontalDivider()
}