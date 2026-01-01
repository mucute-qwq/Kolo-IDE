package io.github.mucute.qwq.koloide.page.main

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.ImportExport
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.mucute.qwq.koloide.R
import io.github.mucute.qwq.koloide.component.FileSelectorDialog
import io.github.mucute.qwq.koloide.component.LoadingContent
import io.github.mucute.qwq.koloide.component.SelectableCard
import io.github.mucute.qwq.koloide.component.SelectableCardDropDownMenu
import io.github.mucute.qwq.koloide.composition.provider.LocalSnackbarHostState
import io.github.mucute.qwq.koloide.manager.ModuleManager
import io.github.mucute.qwq.koloide.model.SelectableCardDropDownMenuItem
import io.github.mucute.qwq.koloide.module.Module
import io.github.mucute.qwq.koloide.module.util.ExtractState
import io.github.mucute.qwq.koloide.module.util.extractBinariesFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private val moduleCardDropDownMenuItems = listOf(
    SelectableCardDropDownMenuItem(
        leadingIcon = Icons.TwoTone.Delete,
        textResId = R.string.uninstall
    )
)

@Composable
fun ModulePage() {
    val moduleState by ModuleManager.state.collectAsStateWithLifecycle()
    val usableModules by ModuleManager.usableModules.collectAsStateWithLifecycle()

    LoadingContent(
        isLoading = moduleState === ModuleManager.State.Processing
    ) {
        if (usableModules.isEmpty()) {
            Text(stringResource(R.string.no_module_installed))
        } else {
            ModuleItems(
                usableModules,
                moduleCardDropDownMenuItems
            )
        }
    }
}

@Composable
private fun ModuleItems(
    usableModules: List<Module>,
    moduleCardDropDownMenuItems: List<SelectableCardDropDownMenuItem>
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(usableModules.size) { index ->
            val usableModule = usableModules[index]
            Column(
                Modifier
                    .fillMaxWidth()
            ) {
                var showModuleDropdownMenu by remember { mutableStateOf(false) }
                SelectableCard(
                    painter = usableModule.icon(),
                    title = stringResource(usableModule.titleResId),
                    subtitle = stringResource(usableModule.subtitleResId),
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    onLongClick = {
                        showModuleDropdownMenu = true
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
                        expanded = showModuleDropdownMenu,
                        onDismissRequest = {
                            showModuleDropdownMenu = false
                        },
                        selectableCardDropDownMenuItems = moduleCardDropDownMenuItems,
                        onClick = {
                            val index = moduleCardDropDownMenuItems.indexOf(it)
                                .takeIf { index -> index >= 0 }
                                ?: return@SelectableCardDropDownMenu
                            when (index) {
                                0 -> {
                                    ModuleManager.uninstall(usableModule)
                                }
                            }
                            showModuleDropdownMenu = false
                        }
                    )
                }
            }
        }
    }
}

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModulePageFloatingActionButton() {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = LocalSnackbarHostState.current
    val context = LocalContext.current
    var isFileSelectorDialogShown by remember { mutableStateOf(false) }
    var extractState: ExtractState by remember { mutableStateOf(ExtractState.Idle) }

    if (extractState is ExtractState.Processing) {
        val processingState = extractState as ExtractState.Processing
        BasicAlertDialog(
            onDismissRequest = {}
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        progress = {
                            processingState.progress
                        },
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        stringResource(R.string.extracting, processingState.name),
                        modifier = Modifier
                            .animateContentSize()
                    )
                }
            }
        }
    }

    FloatingActionButton(
        onClick = {
            isFileSelectorDialogShown = true
        }
    ) {
        Icon(Icons.TwoTone.ImportExport, contentDescription = null)
    }

    if (isFileSelectorDialogShown) {
        FileSelectorDialog(
            onDismissRequest = {
                isFileSelectorDialogShown = false
            },
            selectFile = {
                isFileSelectorDialogShown = false
                if (it.extension != "tar") {
                    coroutineScope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.only_tar_files_can_be_imported)
                        )
                    }
                    return@FileSelectorDialog
                }

                coroutineScope.launch {
                    extractBinariesFlow(context, it.inputStream())
                        .onEach { state ->
                            extractState = state
                        }
                        .onCompletion {
                            ModuleManager.refresh()
                        }
                        .collect()
                }
            }
        )
    }
}