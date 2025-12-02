package io.github.mucute.qwq.koloide.page.main

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ImportExport
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.mucute.qwq.koloide.R
import io.github.mucute.qwq.koloide.component.FileSelectorDialog
import io.github.mucute.qwq.koloide.component.LoadingContent
import io.github.mucute.qwq.koloide.component.SelectableCard
import io.github.mucute.qwq.koloide.component.SelectableCardDropDownMenu
import io.github.mucute.qwq.koloide.composition.provider.LocalSnackbarHostState
import io.github.mucute.qwq.koloide.manager.ModuleManager
import io.github.mucute.qwq.koloide.model.SelectableCardDropDownMenuItem
import io.github.mucute.qwq.koloide.module.Module
import io.github.mucute.qwq.koloide.viewmodel.MainScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun ModulePage() {
    val moduleState by ModuleManager.state.collectAsStateWithLifecycle()
    val modules by ModuleManager.modules.collectAsStateWithLifecycle()
    val viewModel: MainScreenViewModel = viewModel()
    val moduleCardDropDownMenuItems = viewModel.moduleCardDropDownMenuItems
    LoadingContent(
        isLoading = moduleState === ModuleManager.State.Processing
    ) {
        ModuleItems(
            modules,
            moduleCardDropDownMenuItems
        )
    }
}

@Composable
private fun ModuleItems(
    modules: List<Module>,
    moduleCardDropDownMenuItems: List<SelectableCardDropDownMenuItem>
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(modules.size) { index ->
            val module = modules[index]
            Column(
                Modifier
                    .fillMaxWidth()
            ) {
                var showModuleDropdownMenu by remember { mutableStateOf(false) }
                SelectableCard(
                    painter = module.icon(),
                    title = stringResource(module.titleResId),
                    subtitle = stringResource(module.subtitleResId),
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

@Composable
fun ModulePageFloatingActionButton() {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = LocalSnackbarHostState.current
    val context = LocalContext.current
    var isFileSelectorDialogShown by remember { mutableStateOf(false) }

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
                if (it.extension != "tgz") {
                    coroutineScope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.only_tgz_files_can_be_imported)
                        )
                    }
                    return@FileSelectorDialog
                }
            }
        )
    }
}