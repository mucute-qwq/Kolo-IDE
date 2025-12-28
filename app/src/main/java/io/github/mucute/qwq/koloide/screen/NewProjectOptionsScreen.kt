package io.github.mucute.qwq.koloide.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.mucute.qwq.koloide.R
import io.github.mucute.qwq.koloide.composition.provider.LocalNavController
import io.github.mucute.qwq.koloide.composition.provider.LocalSnackbarHostState
import io.github.mucute.qwq.koloide.manager.ModuleManager
import io.github.mucute.qwq.koloide.navigation.NavScreen
import kotlinx.coroutines.flow.map

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProjectOptionsScreen(module: String) {
    val usableModule by ModuleManager.usableModules
        .map { it.find { usableModule -> usableModule.module == module } }
        .collectAsStateWithLifecycle(null)

    val snackbarHostState = remember { SnackbarHostState() }
    val navController = LocalNavController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.project_options))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.AutoMirrored.TwoTone.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            )
        },
        floatingActionButton = {
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                FloatingActionButton(
                    onClick = {
                        if (usableModule?.newProjectOptionsDone() == true) {
                            navController.popBackStack<NavScreen.Main>(
                                inclusive = false,
                                saveState = false
                            )
                            navController.navigate(NavScreen.Workspace)
                        }
                    }
                ) {
                    Icon(Icons.TwoTone.Check, contentDescription = null)
                }
            }
        },
    ) {
        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
            Column(Modifier.padding(it)) {
                usableModule?.NewProjectOptionsContent()
            }
        }
    }
}