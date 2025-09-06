package io.githun.mucute.qwq.koloide.page.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import io.githun.mucute.qwq.koloide.R

@Composable
fun HomePage() {
    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(1) {
                Column(Modifier.fillMaxWidth()) {
                    var showProjectDropdownMenu by remember { mutableStateOf(false) }
                    ProjectCard(
                        rememberVectorPainter(Icons.Rounded.Code),
                        projectName = "Blog $it",
                        projectType = "Vue",
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
                        ProjectCardDropdownMenu(
                            expanded = showProjectDropdownMenu,
                            onDismissRequest = {
                                showProjectDropdownMenu = false
                            },
                            onRename = {},
                            onDelete = {}
                        )
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = {

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
private fun ProjectCardDropdownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.TwoTone.Edit, contentDescription = null)
            },
            text = {
                Text(stringResource(R.string.rename))
            },
            onClick = onRename
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.TwoTone.Delete, contentDescription = null)
            },
            text = {
                Text(stringResource(R.string.delete))
            },
            onClick = onDelete
        )
    }
}

@Composable
private fun ProjectCard(
    painter: Painter,
    projectName: String,
    projectType: String,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                role = Role.Button,
                onLongClick = onLongClick,
                onClick = onClick
            )
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.outlinedCardElevation(),
        ) {
            Row(
                Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.shapes.medium
                        )
                        .size(40.dp)
                )
                Spacer(
                    Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                )
                Column(Modifier.weight(1f)) {
                    Text(
                        projectName,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(1.5.dp)
                    )
                    Text(
                        stringResource(R.string.project_type, projectType),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}