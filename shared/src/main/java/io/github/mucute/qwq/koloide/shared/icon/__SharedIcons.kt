package io.github.mucute.qwq.koloide.shared.icon

import androidx.compose.ui.graphics.vector.ImageVector
import io.github.mucute.qwq.koloide.shared.icon.sharedicons.Html24
import io.github.mucute.qwq.koloide.shared.icon.sharedicons.Nodejs24
import io.github.mucute.qwq.koloide.shared.icon.sharedicons.OutlinePackage224
import kotlin.collections.List as ____KtList

public object SharedIcons

private var __AllIcons: ____KtList<ImageVector>? = null

public val SharedIcons.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf(Html24, Nodejs24, OutlinePackage224)
    return __AllIcons!!
  }
