package io.github.mucute.qwq.koloide.module

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import java.io.File

data class ProjectExtra(
    val module: Module,
    val extras: JsonObject
) {

    companion object {

        private val json = Json { prettyPrint = true }

        fun fetch(projectFolder: File, modules: List<Module>): ProjectExtra? {
            val projectHiddenFolder = File(projectFolder, ".project")
            if (projectFolder.isFile || !projectFolder.exists()) {
                return null
            }

            val projectExtrasFile = File(projectHiddenFolder, "projectExtras.json")
            if (projectExtrasFile.isDirectory || !projectExtrasFile.exists()) {
                return null
            }

            return fromJson(projectExtrasFile.readText(), modules)
        }

        fun fromJson(string: String, modules: List<Module>) = try {
            val jsonObject = json.parseToJsonElement(string).jsonObject
            val type = jsonObject["type"]!!.jsonPrimitive.content
            val extras = jsonObject["extras"]!!.jsonObject
            val module = modules.find { it.type == type }!!
            ProjectExtra(module, extras)
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }

    }

    fun toJson() = json.encodeToString(
        buildJsonObject {
            put("type", module.type)
            put("extras", JsonObject(emptyMap()))
        }
    )

    fun save(projectFolder: File) {
        val projectHiddenFolder = File(projectFolder, ".project")
        projectHiddenFolder.mkdirs()

        val projectExtrasFile = File(projectHiddenFolder, "projectExtras.json")
        projectExtrasFile.writeText(toJson())
    }

}