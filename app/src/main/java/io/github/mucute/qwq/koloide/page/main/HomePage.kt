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
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.mucute.qwq.koloide.R
import io.github.mucute.qwq.koloide.component.LoadingContent
import io.github.mucute.qwq.koloide.component.SelectableCard
import io.github.mucute.qwq.koloide.component.SelectableCardDropDownMenu
import io.github.mucute.qwq.koloide.composition.provider.LocalNavController
import io.github.mucute.qwq.koloide.manager.ProjectManager
import io.github.mucute.qwq.koloide.model.SelectableCardDropDownMenuItem
import io.github.mucute.qwq.koloide.module.Project
import io.github.mucute.qwq.koloide.navigation.NavScreen

private val projectCardDropDownMenuItems = listOf(
    SelectableCardDropDownMenuItem(
        leadingIcon = Icons.TwoTone.Edit,
        textResId = R.string.rename
    ),
    SelectableCardDropDownMenuItem(
        leadingIcon = Icons.TwoTone.Delete,
        textResId = R.string.delete
    ),
)

@Composable
fun HomePage() {
    val projectState by ProjectManager.state.collectAsStateWithLifecycle()
    val usableProjects by ProjectManager.usableProjects.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    LoadingContent(
        isLoading = projectState === ProjectManager.State.Processing
    ) {
        if (usableProjects.isEmpty()) {
            Text(stringResource(R.string.no_project_created))
        } else {
            ProjectItems(
                usableProjects
            )
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

@Composable
private fun ProjectItems(
    usableProjects: List<Project>
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(usableProjects.size) {
            val usableProject = usableProjects[it]
            val projectExtra = usableProject.projectExtra
            Column(
                Modifier
                    .fillMaxWidth()
            ) {
                var showProjectDropdownMenu by retain { mutableStateOf(false) }
                SelectableCard(
                    rememberVectorPainter(Icons.Rounded.Code),
                    title = usableProject.name,
                    subtitle = projectExtra.module.type,
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    onLongClick = {
                        showProjectDropdownMenu = true
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
                        expanded = showProjectDropdownMenu,
                        onDismissRequest = {
                            showProjectDropdownMenu = false
                        },
                        selectableCardDropDownMenuItems = projectCardDropDownMenuItems,
                        onClick = { index, _ ->
                            when (index) {
                                0 -> {}
                                1 -> ProjectManager.delete(usableProject)
                            }
                        }
                    )
                }
            }
        }
    }
}