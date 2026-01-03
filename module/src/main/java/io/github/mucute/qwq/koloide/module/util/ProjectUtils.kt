package io.github.mucute.qwq.koloide.module.util

import android.content.Context
import io.github.mucute.qwq.koloide.module.Module
import io.github.mucute.qwq.koloide.module.R
import java.io.File

fun validateProjectOptions(
    context: Context,
    projectName: String,
    projectVersion: String
): String? {
    if (projectName.isEmpty()) {
        return context.getString(R.string.project_name_cannot_be_null)
    }

    if (projectVersion.isEmpty()) {
        return context.getString(R.string.project_version_cannot_be_empty)
    }

    if ("/" in projectName) {
        return context.getString(R.string.invalid_project_name)
    }

    if (File(context.filesDir, "home").resolve(projectName).exists()) {
        return context.getString(R.string.project_already_exists)
    }

    return null
}