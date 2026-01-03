package io.github.mucute.qwq.koloide.manager

import io.github.mucute.qwq.koloide.application.AppContext
import io.github.mucute.qwq.koloide.module.Project
import io.github.mucute.qwq.koloide.module.ProjectExtra
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

object ProjectManager {

    enum class State {
        Idle, Processing
    }

    private val _state = MutableStateFlow(State.Processing)

    val state = _state.asStateFlow()

    private val _usableProjects = MutableStateFlow(emptyList<Project>())

    val usableProjects = _usableProjects.asStateFlow()

    fun rename(project: Project, targetProjectName: String) {
        AppContext.appScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            _state.update { State.Processing }
            renameSuspend(project, targetProjectName)
        }.invokeOnCompletion {
            _state.update { State.Idle }
        }
    }

    private suspend fun renameSuspend(project: Project, targetProjectName: String) {
        withContext(Dispatchers.IO) {
            _usableProjects.update { usableProjects ->
                val homeFolder = File(AppContext.instance.filesDir, "home")
                val projectFolder = File(homeFolder, project.name)
                val targetProjectFolder = File(homeFolder, targetProjectName)
                projectFolder.copyRecursively(targetProjectFolder)

                usableProjects.toMutableList()
                    .apply {
                        remove(project)
                        add(project.copy(name = targetProjectName))
                    }
                    .toList()
                    .sortedBy { it.name }
            }
        }
    }

    fun delete(project: Project) {
        AppContext.appScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            _state.update { State.Processing }
            deleteSuspend(project)
        }.invokeOnCompletion {
            _state.update { State.Idle }
        }
    }

    private suspend fun deleteSuspend(project: Project) {
        withContext(Dispatchers.IO) {
            _usableProjects.update { usableProjects ->
                val homeFolder = File(AppContext.instance.filesDir, "home")
                val projectFolder = File(homeFolder, project.name)
                projectFolder.deleteRecursively()

                usableProjects.toMutableList()
                    .apply { remove(project) }
                    .toList()
                    .sortedBy { it.name }
            }
        }
    }

    fun refresh() {
        AppContext.appScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            _state.update { State.Processing }
            refreshSuspend()
        }.invokeOnCompletion {
            _state.update { State.Idle }
        }
    }

    private suspend fun refreshSuspend() {
        withContext(Dispatchers.IO) {
            _usableProjects.update {
                buildList {
                    val homeFolder = File(AppContext.instance.filesDir, "home")
                    val children = homeFolder.listFiles() ?: emptyArray()

                    for (projectFolder in children) {
                        val projectExtra =
                            ProjectExtra.fetch(projectFolder, ModuleManager.usableModules.value)
                                ?: continue

                        val project = Project(projectFolder.name, projectExtra)
                        add(project)
                    }
                }.sortedBy { it.name }
            }
        }
    }

}