package io.github.mucute.qwq.koloide.module.nodejs

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.mucute.qwq.koloide.module.Module
import io.github.mucute.qwq.koloide.module.nodejs.template.ProjectTemplates

class NodeJSModule(application: Application) : Module(
    titleResId = R.string.module_title,
    subtitleResId = R.string.module_subtitle
) {
    override val module = "nodejs"


    private var projectName by mutableStateOf("")

    private var projectVersion by mutableStateOf("1.0.0")

    private var projectDescription by mutableStateOf("")

    private var projectTemplate by mutableStateOf(ProjectTemplates.first())

    @Composable
    override fun newProjectIcon(): Painter {
        return painterResource(R.drawable.node_js_brands_solid_full)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun NewProjectOptionsContent() {
        var isExposedDropdownMenuExpanded by remember { mutableStateOf(false) }

        DisposableEffect(Unit) {
            onDispose {
                projectName = ""
                projectVersion = ""
                projectDescription = ""
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = projectName,
                label = {
                    Text(stringResource(R.string.project_name))
                },
                singleLine = true,
                onValueChange = {
                    projectName = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
            TextField(
                value = projectVersion,
                label = {
                    Text(stringResource(R.string.project_version))
                },
                singleLine = true,
                onValueChange = {
                    projectVersion = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
            TextField(
                value = projectDescription,
                label = {
                    Text(stringResource(R.string.project_description))
                },
                singleLine = true,
                onValueChange = {
                    projectDescription = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
            ExposedDropdownMenuBox(
                expanded = isExposedDropdownMenuExpanded,
                onExpandedChange = {
                    isExposedDropdownMenuExpanded = it
                }
            ) {
                TextField(
                    value = stringResource(projectTemplate.nameResId),
                    onValueChange = {},
                    readOnly = true,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .onFocusEvent {
                                if (it.isFocused) {
                                    isExposedDropdownMenuExpanded = true
                                }
                            }
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
                    label = {
                        Text(stringResource(R.string.project_template))
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isExposedDropdownMenuExpanded,
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.SecondaryEditable),
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                )
                ExposedDropdownMenu(
                    expanded = isExposedDropdownMenuExpanded,
                    onDismissRequest = {
                        isExposedDropdownMenuExpanded = false
                    }
                ) {
                    ProjectTemplates.forEach { projectTemplate ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        stringResource(projectTemplate.nameResId),
                                        style = MaterialTheme.typography.bodyLarge,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        stringResource(projectTemplate.descriptionResId),
                                        style = MaterialTheme.typography.bodySmall,
//                                        maxLines = 1,
//                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            },
                            onClick = {
                                isExposedDropdownMenuExpanded = false
                            },
                            contentPadding = PaddingValues(horizontal = 16.dp, 8.dp),
                        )
                    }
                }
            }
        }
    }

    override fun newProjectOptionsDone(): Boolean {
        return super.newProjectOptionsDone()
    }

}