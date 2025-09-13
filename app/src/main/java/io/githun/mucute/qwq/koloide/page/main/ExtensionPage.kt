package io.githun.mucute.qwq.koloide.page.main

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
import androidx.lifecycle.viewmodel.compose.viewModel
import io.githun.mucute.qwq.koloide.component.SelectableCard
import io.githun.mucute.qwq.koloide.component.SelectableCardDropDownMenu
import io.githun.mucute.qwq.koloide.viewmodel.MainScreenViewModel

@Composable
fun ExtensionPage() {
    val viewModel: MainScreenViewModel = viewModel()
    val extensionCardDropDownMenuItems = viewModel.extensionCardDropDownMenuItems
    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(1) {
                Column(Modifier.fillMaxWidth()) {
                    var showExtensionDropdownMenu by remember { mutableStateOf(false) }
                    SelectableCard(
                        rememberVectorPainter(Icons.Rounded.Code),
                        title = "Blog $it",
                        subtitle = "Vue",
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

                            }
                        )
                    }
                }
            }
        }
    }
}