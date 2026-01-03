package io.github.mucute.qwq.koloide.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.mucute.qwq.koloide.R
import io.github.mucute.qwq.koloide.component.GalleryCard
import io.github.mucute.qwq.koloide.component.LoadingContent
import io.github.mucute.qwq.koloide.composition.provider.LocalNavController
import io.github.mucute.qwq.koloide.manager.ModuleManager
import io.github.mucute.qwq.koloide.module.Module
import io.github.mucute.qwq.koloide.navigation.NavScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProjectScreen() {
    val moduleState by ModuleManager.state.collectAsStateWithLifecycle()
    val usableModules by ModuleManager.usableModules.collectAsStateWithLifecycle()

    val navController = LocalNavController.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.new_project))
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
        }
    ) {
        Column(Modifier.padding(it)) {
            LoadingContent(
                isLoading = moduleState === ModuleManager.State.Processing
            ) {
                if (usableModules.isEmpty()) {
                    Text(stringResource(R.string.no_module_installed))
                } else {
                    NewProjectItems(usableModules)
                }
            }
        }
    }
}

@Composable
private fun NewProjectItems(
    usableModules: List<Module>
) {
    val navController = LocalNavController.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(usableModules.size) { index ->
            val usableModule = usableModules[index]
            GalleryCard(
                painter = usableModule.newProjectIcon(),
                onClick = {
                    navController.navigate(NavScreen.NewProjectOptions(usableModule.type))
                }
            )
        }
    }
}