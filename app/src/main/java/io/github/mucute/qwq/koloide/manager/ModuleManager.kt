package io.github.mucute.qwq.koloide.manager

import android.util.Log
import io.github.mucute.qwq.koloide.application.AppContext
import io.github.mucute.qwq.koloide.module.Module
import io.github.mucute.qwq.koloide.module.nodejs.NodeJSModule
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    fun refresh() {
        AppContext.appScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            _state.update { State.Processing }
            _usableModules.update {
                buildList {
                    val moduleFolder = File(AppContext.instance.filesDir, "module")
                    val children = moduleFolder.listFiles() ?: emptyArray()
                    for (child in children) {
                        val jsonObject = Json.parseToJsonElement(child.readText()).jsonObject
                        val module = jsonObject["module"]!!.jsonPrimitive.content
                        val targetModule = modules.value.find { it.module == module } ?: continue
                        Log.e("Module", targetModule.toString())
                        add(targetModule)
                    }
                }
            }
        }.invokeOnCompletion {
            _state.update { State.Idle }
        }
    }

}