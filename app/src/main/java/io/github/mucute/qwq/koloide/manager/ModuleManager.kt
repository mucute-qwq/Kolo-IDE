package io.github.mucute.qwq.koloide.manager

import io.github.mucute.qwq.koloide.application.AppContext
import io.github.mucute.qwq.koloide.module.nodejs.NodeJSModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

}