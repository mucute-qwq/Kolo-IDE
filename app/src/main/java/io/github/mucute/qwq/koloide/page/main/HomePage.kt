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
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import io.github.mucute.qwq.koloide.component.SelectableCard
import io.github.mucute.qwq.koloide.component.SelectableCardDropDownMenu
import io.github.mucute.qwq.koloide.navigation.LocalNavController
import io.github.mucute.qwq.koloide.navigation.NavScreen
import io.github.mucute.qwq.koloide.viewmodel.MainScreenViewModel

@Composable
fun HomePage() {
    val navController = LocalNavController.current
    val viewModel: MainScreenViewModel = viewModel()
    val projectCardDropDownMenuItems = viewModel.projectCardDropDownMenuItems
    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(1) { item ->
                Column(
                    Modifier
                        .fillMaxWidth()
                ) {
                    var showProjectDropdownMenu by remember { mutableStateOf(false) }
                    SelectableCard(
                        rememberVectorPainter(Icons.Rounded.Code),
                        title = "Blog $item",
                        subtitle = "Vue",
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        onLongClick = {
                            showProjectDropdownMenu = true
                        },
                        onClick = {
                            navController.navigate(NavScreen.LspTest)
                        }
                    )
                    Box(
                        Modifier
                            .padding(horizontal = 16.dp)
                            .align(Alignment.End)
                    ) {
                        SelectableCardDropDownMenu(
                            expanded = showProjectDropdownMenu,
                            onDismissRequest = {
                                showProjectDropdownMenu = false
                            },
                            selectableCardDropDownMenuItems = projectCardDropDownMenuItems,
                            onClick = { projectCardDropDownMenuItem ->
                                val index =
                                    projectCardDropDownMenuItems.indexOf(projectCardDropDownMenuItem)
                                        .takeIf { it >= 0 } ?: return@SelectableCardDropDownMenu

                            }
                        )
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = {
                navController.navigate(NavScreen.NewProject)
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.TwoTone.Add, contentDescription = null)
        }
    }
}