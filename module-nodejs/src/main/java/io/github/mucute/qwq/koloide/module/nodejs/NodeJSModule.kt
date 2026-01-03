package io.github.mucute.qwq.koloide.module.nodejs

import android.app.Application
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Done
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.mucute.qwq.koloide.module.Module
import io.github.mucute.qwq.koloide.module.ProjectExtra
import io.github.mucute.qwq.koloide.module.component.ExtractDialog
import io.github.mucute.qwq.koloide.module.nodejs.template.ProjectTemplate
import io.github.mucute.qwq.koloide.module.nodejs.template.ProjectTemplates
import io.github.mucute.qwq.koloide.module.util.ExtractState
import io.github.mucute.qwq.koloide.module.util.extractFilesFlow
import io.github.mucute.qwq.koloide.module.util.validateProjectOptions
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import java.io.File

class NodeJSModule(application: Application) : Module(
    application = application,
    titleResId = R.string.module_title,
    subtitleResId = R.string.module_subtitle
) {
    override val type = "nodejs"

    private val snackbarHostState = SnackbarHostState()

    @Composable
    override fun newProjectIcon(): Painter {
        return painterResource(R.drawable.node_js_brands_solid_full)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun NewProjectOptionsContent(done: () -> Unit) {
        val coroutineScope = rememberCoroutineScope()
        val scrollState = retain { ScrollState(0) }
        val (extractState, setExtractState) = retain { mutableStateOf<ExtractState>(ExtractState.Idle) }
        var projectName by retain { mutableStateOf("") }
        var projectVersion by retain { mutableStateOf("1.0.0") }
        var projectDescription by retain { mutableStateOf("") }
        var projectTemplate by retain { mutableStateOf(ProjectTemplates.first()) }
        var isExposedDropdownMenuExpanded by retain { mutableStateOf(false) }

        Box(
            Modifier
                .imePadding()
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState),
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
                        ProjectTemplates.forEach { template ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(
                                            stringResource(template.nameResId),
                                            style = MaterialTheme.typography.bodyLarge,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            stringResource(template.descriptionResId),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                },
                                onClick = {
                                    projectTemplate = template
                                    isExposedDropdownMenuExpanded = false
                                },
                                contentPadding = PaddingValues(horizontal = 16.dp, 8.dp),
                            )
                        }
                    }
                }
            }

            Column(
                Modifier
                    .align(Alignment.BottomCenter)
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            val errorMessage = validateProjectOptions(
                                application,
                                projectName,
                                projectVersion
                            ) ?: return@launch createProjectImpl(
                                projectName,
                                projectVersion,
                                projectDescription,
                                projectTemplate,
                                setExtractState,
                                done
                            )

                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(errorMessage)
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.End)
                ) {
                    Icon(Icons.TwoTone.Done, contentDescription = null)
                }

                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                )
            }
        }

        ExtractDialog(extractState)
    }

    private suspend fun createProjectImpl(
        projectName: String,
        projectVersion: String,
        projectDescription: String,
        projectTemplate: ProjectTemplate,
        setExtractState: (ExtractState) -> Unit,
        done: () -> Unit
    ) {
        supervisorScope {
            fun showErrorMessage(throwable: Throwable) {
                launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        application.getString(
                            R.string.project_creation_failed,
                            throwable.message.toString()
                        )
                    )
                }
            }

            launch(CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
                showErrorMessage(throwable)
            }) {
                val homeFolder = application.filesDir.resolve("home")
                val projectFolder = File(homeFolder, projectName)
                projectFolder.mkdirs()

                extractFilesFlow(
                    application.assets.open(projectTemplate.path),
                    projectFolder
                ).collect {
                    setExtractState(it)
                }

                withContext(Dispatchers.IO) {
                    val packageJsonFile = File(projectFolder, "package.json")
                    packageJsonFile.writeText(
                        packageJsonFile.readText()
                            .replace($$"${PROJECT_NAME}", projectName)
                            .replace($$"${PROJECT_VERSION}", projectVersion)
                            .replace($$"${PROJECT_DESCRIPTION}", projectDescription)
                    )

                    ProjectExtra(this@NodeJSModule, JsonObject(emptyMap()))
                        .save(projectFolder)
                }

                done()

            }.invokeOnCompletion {
                setExtractState(ExtractState.Idle)
            }
        }
    }

}