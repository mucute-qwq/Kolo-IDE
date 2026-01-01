package io.github.mucute.qwq.koloide.module.util

sealed class ExtractState {

    data class Processing(
        val name: String,
        val progress: Float
    ) : ExtractState()

    object Idle : ExtractState()

}