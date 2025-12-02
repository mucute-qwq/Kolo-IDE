package io.github.mucute.qwq.koloide.composition.provider

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController

@Suppress("SameParameterValue")
private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}

val LocalNavController =
    compositionLocalOf<NavController> { noLocalProvidedFor("LocalNavController") }

val LocalSnackbarHostState =
    compositionLocalOf<SnackbarHostState> { noLocalProvidedFor("LocalSnackbarHostState") }