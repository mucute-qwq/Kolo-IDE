package io.github.mucute.qwq.koloide.component

import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.InsertDriveFile
import androidx.compose.material.icons.twotone.ArrowUpward
import androidx.compose.material.icons.twotone.Folder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.mucute.qwq.koloide.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileSelectorDialog(
    onDismissRequest: () -> Unit,
    folder: File = Environment.getExternalStorageDirectory(),
    navigateUp: (currentFolder: File, parentFolder: File) -> File = { currentFolder, parentFolder -> if (currentFolder.absolutePath == Environment.getExternalStorageDirectory().absolutePath) currentFolder else parentFolder },
    navigateIn: (currentFolder: File, targetFolder: File) -> File = { _, targetFolder -> targetFolder },
    selectFile: (file: File) -> Unit = {}
) {
    var currentFolder by retain { mutableStateOf(folder) }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        val parentFolder = currentFolder.parentFile ?: return@IconButton
                        currentFolder = navigateUp(currentFolder, parentFolder)
                    }
                ) {
                    Icon(Icons.TwoTone.ArrowUpward, contentDescription = null)
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(stringResource(R.string.file_selector))
                    Text(
                        currentFolder.absolutePath,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        },

        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(
                            constraints.copy(
                                maxWidth = constraints.maxWidth + 24.dp.roundToPx() * 2,
                            )
                        )
                        layout(placeable.width, placeable.height) {
                            placeable.place(0, 0)
                        }
                    }
            ) {
                FileSelectorDialogContent(
                    currentFolder = currentFolder,
                    changeCurrentFolder = {
                        currentFolder = navigateIn(currentFolder, it)
                    },
                    selectFile = selectFile,
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        modifier = Modifier
            .padding(vertical = 24.dp)
    )
}

@Composable
private fun FileSelectorDialogContent(
    currentFolder: File,
    changeCurrentFolder: (folder: File) -> Unit,
    selectFile: (file: File) -> Unit
) {
    val children by produceState(emptyList(), currentFolder) {
        value = withContext(Dispatchers.IO) {
            (currentFolder.listFiles() ?: emptyArray())
                .sortedBy { it.isFile }
        }
    }

    LazyColumn {
        items(children.size) {
            val child = children[it]
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clickable {
                        if (child.isDirectory) {
                            changeCurrentFolder(child)
                        } else {
                            selectFile(child)
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(24.dp))
                Icon(
                    if (child.isFile) Icons.AutoMirrored.TwoTone.InsertDriveFile else Icons.TwoTone.Folder,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .scale(0.6f)
                        .size(36.dp)
                )
                Spacer(Modifier.width(24.dp))
                Text(
                    child.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.width(24.dp))
            }
        }
    }
}