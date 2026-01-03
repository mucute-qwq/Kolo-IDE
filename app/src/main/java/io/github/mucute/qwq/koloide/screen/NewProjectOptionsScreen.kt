package io.github.mucute.qwq.koloide.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.mucute.qwq.koloide.R
import io.github.mucute.qwq.koloide.composition.provider.LocalNavController
import io.github.mucute.qwq.koloide.manager.ModuleManager
import io.github.mucute.qwq.koloide.manager.ProjectManager
import io.github.mucute.qwq.koloide.module.Module
import io.github.mucute.qwq.koloide.navigation.NavScreen

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProjectOptionsScreen(module: String) {
    val usableModules by ModuleManager.usableModules.collectAsStateWithLifecycle()
    val usableModule: Module? by produceState(null, usableModules) {
        value = usableModules.find { it.type == module }
    }
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
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            usableModule?.NewProjectOptionsContent {
                ProjectManager.refresh()
                navController.popBackStack<NavScreen.Main>(
                    inclusive = false,
                    saveState = false
                )
                navController.navigate(NavScreen.Workspace)
            }
        }
    }
}