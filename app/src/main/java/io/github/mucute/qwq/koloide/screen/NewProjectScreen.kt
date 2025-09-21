package io.github.mucute.qwq.koloide.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Code
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.mucute.qwq.koloide.navigation.LocalNavController
import io.github.mucute.qwq.koloide.R
import io.github.mucute.qwq.koloide.component.GalleryCard
import io.github.mucute.qwq.koloide.component.LoadingContent
import io.github.mucute.qwq.koloide.component.useExtensionContext
import io.github.mucute.qwq.koloide.manager.ExtensionManager
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProjectScreen() {
    val extensionState by ExtensionManager.state.collectAsStateWithLifecycle()
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
                isLoading = extensionState === ExtensionManager.State.Processing
            ) {
                NewProjectItems()
            }
        }
    }
}

@Composable
private fun NewProjectItems() {
    val extensions by ExtensionManager.extensions
        .map { it.filter { extension -> extension.extensionMain.extensionProject() != null } }
        .collectAsStateWithLifecycle(emptyList())

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(extensions.size) { index ->
            val extension = extensions[index]
            val extensionProject = extension.extensionMain.extensionProject() ?: return@items
            GalleryCard(
                painter = extension.useExtensionContext {
                    extensionProject.newProjectIcon()
                },
                onClick = {

                }
            )
        }
    }
}