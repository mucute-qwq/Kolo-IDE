package io.github.mucute.qwq.koloide.page.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Code
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.mucute.qwq.koloide.extension.description
import io.github.mucute.qwq.koloide.extension.label

@Composable
fun ExtensionPage() {
    val extensions by _root_ide_package_.io.github.mucute.qwq.koloide.manager.ExtensionManager.extensions.collectAsStateWithLifecycle()
    val viewModel: io.github.mucute.qwq.koloide.viewmodel.MainScreenViewModel = viewModel()
    val extensionCardDropDownMenuItems = viewModel.extensionCardDropDownMenuItems
    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(extensions.size) { index ->
                val extension = extensions[index]
                Column(Modifier.fillMaxWidth()) {
                    var showExtensionDropdownMenu by remember { mutableStateOf(false) }
                    _root_ide_package_.io.github.mucute.qwq.koloide.component.SelectableCard(
                        rememberVectorPainter(Icons.Rounded.Code),
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
                        _root_ide_package_.io.github.mucute.qwq.koloide.component.SelectableCardDropDownMenu(
                            expanded = showExtensionDropdownMenu,
                            onDismissRequest = {
                                showExtensionDropdownMenu = false
                            },
                            selectableCardDropDownMenuItems = extensionCardDropDownMenuItems,
                            onClick = {

                            }
                        )
                    }
                }
            }
        }
    }
}