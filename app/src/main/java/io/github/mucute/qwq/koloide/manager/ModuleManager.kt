package io.github.mucute.qwq.koloide.manager

import io.github.mucute.qwq.koloide.application.AppContext
import io.github.mucute.qwq.koloide.module.Module
import io.github.mucute.qwq.koloide.module.nodejs.NodeJSModule
import io.github.mucute.qwq.koloide.module.util.ExtractState
import io.github.mucute.qwq.koloide.module.util.extractBinariesFlow
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File

object ModuleManager {

    enum class State {
        Idle, Processing
    }

    private val _state = MutableStateFlow(State.Idle)

    val state = _state.asStateFlow()

    private val _modules = MutableStateFlow(
        listOf(
            NodeJSModule(AppContext.instance)
        )
    )

    val modules = _modules.asStateFlow()

    private val _usableModules = MutableStateFlow(
        emptyList<Module>()
    )

    val usableModules = _usableModules.asStateFlow()

    fun install(
        file: File,
        onCollected: FlowCollector<ExtractState>
    ) {
        AppContext.appScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            _state.update { State.Processing }
            installSuspend(file, onCollected)
        }.invokeOnCompletion {
            _state.update { State.Idle }
            ProjectManager.refresh()
        }
    }

    private suspend fun installSuspend(
        file: File,
        onCollected: FlowCollector<ExtractState>
    ) {
        withContext(Dispatchers.IO) {
            extractBinariesFlow(
                AppContext.instance,
                file.inputStream()
            ).collect(onCollected)

            refreshSuspend()
        }
    }

    fun uninstall(module: Module) {
        AppContext.appScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            _state.update { State.Processing }
            uninstallSuspend(module)
        }.invokeOnCompletion {
            _state.update { State.Idle }
            ProjectManager.refresh()
        }
    }

    private suspend fun uninstallSuspend(module: Module) {
        withContext(Dispatchers.IO) {
            _usableModules.update { usableModules ->
                val moduleFolder = File(AppContext.instance.filesDir, "module")
                val controlFolder = File(AppContext.instance.filesDir, "control")
                val usrFolder = File(AppContext.instance.filesDir, "usr")
                val excludedFiles = (controlFolder.listFiles() ?: emptyArray())
                    .filter { it.name != "${module.type}.mappings" }
                    .flatMap { it.readLines() }
                    .filter { it.isNotEmpty() }
                    .toHashSet()

                File(AppContext.instance.filesDir, "test.mappings")
                    .writeText(excludedFiles.joinToString(separator = "\n"))

                File(moduleFolder, "${module.type}-module-info.json").delete()
                File(controlFolder, "${module.type}.mappings").apply {
                    forEachLine {
                        if (it.isEmpty()) {
                            return@forEachLine
                        }

                        val file = File(it)
                        if (file.exists() && it !in excludedFiles) {
                            file.delete()
                        }
                    }

                    for (file in usrFolder.walkBottomUp()) {
                        if (file.isDirectory && (file.listFiles() ?: emptyArray()).isEmpty()) {
                            file.delete()
                        }
                    }

                    delete()
                }

                usableModules
                    .toMutableList()
                    .apply { remove(module) }
                    .toList()
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
            ProjectManager.refresh()
        }
    }

    private suspend fun refreshSuspend() {
        withContext(Dispatchers.IO) {
            _usableModules.update {
                buildList {
                    val moduleFolder = File(AppContext.instance.filesDir, "module")
                    val children = moduleFolder.listFiles() ?: emptyArray()
                    for (child in children) {
                        val jsonObject = Json.parseToJsonElement(child.readText()).jsonObject
                        val type = jsonObject["type"]!!.jsonPrimitive.content
                        val module = modules.value.find { it.type == type } ?: continue
                        add(module)
                    }
                }
            }
        }
    }

}