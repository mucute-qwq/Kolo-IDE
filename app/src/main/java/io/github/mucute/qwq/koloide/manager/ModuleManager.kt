package io.github.mucute.qwq.koloide.manager

import io.github.mucute.qwq.koloide.module.nodejs.NodeJSModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object ModuleManager {

    enum class State {
        Idle, Processing
    }

    private val _state = MutableStateFlow(State.Idle)

    val state = _state.asStateFlow()

    private val _modules = MutableStateFlow(
        listOf(
            NodeJSModule
        )
    )

    val modules = _modules.asStateFlow()

}