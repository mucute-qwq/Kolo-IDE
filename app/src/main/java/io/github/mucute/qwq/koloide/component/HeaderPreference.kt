package io.github.mucute.qwq.koloide.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import me.zhanghai.compose.preference.LocalPreferenceTheme
import me.zhanghai.compose.preference.Preference

fun LazyListScope.headerPreference(
    key: String,
    summary: @Composable () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    item(key = key, contentType = "HeaderPreference") {
        HeaderPreference(summary = summary, modifier = modifier)
    }
}

@Composable
fun HeaderPreference(
    summary: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val paddingValues = LocalPreferenceTheme.current.padding
    val layoutDirection = LocalLayoutDirection.current
    val start = paddingValues.calculateStartPadding(layoutDirection)
    val end = paddingValues.calculateEndPadding(layoutDirection)
    val top = paddingValues.calculateTopPadding()
    val bottom = paddingValues.calculateBottomPadding() / 2
    CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyMedium) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
            Column(
                modifier = modifier
                    .padding(start = start, end = end, top = top, bottom = bottom)
            ) {
                summary()
            }
        }
    }
}