package io.github.mucute.qwq.koloide.page.main

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.mucute.qwq.koloide.component.LoadingContent
import io.github.mucute.qwq.koloide.component.SelectableCard
import io.github.mucute.qwq.koloide.component.SelectableCardDropDownMenu
import io.github.mucute.qwq.koloide.component.useExtensionContext
import io.github.mucute.qwq.koloide.extension.description
import io.github.mucute.qwq.koloide.extension.label
import io.github.mucute.qwq.koloide.extension.packageName
import io.github.mucute.qwq.koloide.manager.ExtensionManager
import io.github.mucute.qwq.koloide.viewmodel.MainScreenViewModel

@Composable
fun ExtensionPage() {
    val extensionState by ExtensionManager.state.collectAsStateWithLifecycle()
    LoadingContent(extensionState === ExtensionManager.State.Processing) {
        ExtensionItems()
    }
}

@Composable
private fun ExtensionItems() {
    val extensions by ExtensionManager.extensions.collectAsStateWithLifecycle()
    val viewModel: MainScreenViewModel = viewModel()
    val extensionCardDropDownMenuItems = viewModel.extensionCardDropDownMenuItems
    val context = LocalContext.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(extensions.size) { index ->
            val extension = extensions[index]
            Column(
                Modifier
                    .fillMaxWidth()
            ) {
                var showExtensionDropdownMenu by remember { mutableStateOf(false) }
                SelectableCard(
                    painter = extension.useExtensionContext {
                        it.extensionMain.icon()
                    },
                    title = extension.label,
                    subtitle = extension.description,
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    onLongClick = {
                        showExtensionDropdownMenu = true
                    },
                    onClick = {

                    }
                )
                Box(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.End)
                ) {
                    SelectableCardDropDownMenu(
                        expanded = showExtensionDropdownMenu,
                        onDismissRequest = {
                            showExtensionDropdownMenu = false
                        },
                        selectableCardDropDownMenuItems = extensionCardDropDownMenuItems,
                        onClick = {
                            val index = extensionCardDropDownMenuItems.indexOf(it)
                                .takeIf { index -> index >= 0 } ?: return@SelectableCardDropDownMenu
                            when (index) {
                                0 -> {
                                    context.startActivity(Intent(Intent.ACTION_DELETE).apply {
                                        data = "package:${extension.packageName}".toUri()
                                    })
                                }
                            }
                            showExtensionDropdownMenu = false
                        }
                    )
                }
            }
        }
    }
}