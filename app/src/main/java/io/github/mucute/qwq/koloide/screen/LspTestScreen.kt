package io.github.mucute.qwq.koloide.screen

import android.graphics.Typeface
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import io.github.mucute.qwq.koloide.R
import io.github.rosemoe.sora.langs.monarch.MonarchColorScheme
import io.github.rosemoe.sora.langs.monarch.MonarchLanguage
import io.github.rosemoe.sora.langs.monarch.registry.ThemeRegistry
import io.github.rosemoe.sora.lsp.client.connection.LocalSocketStreamConnectionProvider
import io.github.rosemoe.sora.lsp.client.languageserver.serverdefinition.CustomLanguageServerDefinition
import io.github.rosemoe.sora.lsp.client.languageserver.serverdefinition.LanguageServerDefinition
import io.github.rosemoe.sora.lsp.editor.LspProject
import io.github.rosemoe.sora.widget.CodeEditor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LspTestScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                }
            )
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            CodeEditor()
        }
    }
}

@Composable
private fun CodeEditor() {
    AndroidView(
        factory = {
            val typeface = Typeface.createFromAsset(it.assets, "font/JetBrainsMono-Regular.ttf")
            val codeEditor = CodeEditor(it).apply {
                typefaceLineNumber = typeface
                typefaceText = typeface
                colorScheme = MonarchColorScheme.create(ThemeRegistry.currentTheme)
                setEditorLanguage(MonarchLanguage.create("source.javascript", true))
            }

            val projectFolder = it.filesDir.resolve("test")
            projectFolder.mkdirs()

            val packageFile = projectFolder.resolve("package.json")
            packageFile.writeText("""
                {
                  "name": "test",
                  "version": "1.0.0",
                  "description": "",
                  "license": "ISC",
                  "author": "",
                  "type": "module",
                  "main": "index.js",
                  "scripts": {
                    "start": "node index.js"
                  }
                }
            """.trimIndent())

            val mainFile = projectFolder.resolve("index.js")
            mainFile.createNewFile()

            val lspProject = LspProject(projectFolder.absolutePath)
            lspProject.addServerDefinition(object : CustomLanguageServerDefinition(
                "javascript",
                ServerConnectProvider {
                    LocalSocketStreamConnectionProvider("")
                }
            ) {

            })

            val lspEditor = lspProject.createEditor(mainFile.absolutePath)
            lspEditor.editor = codeEditor
            lspEditor.wrapperLanguage = codeEditor.editorLanguage
            lspEditor.isEnableInlayHint = true
            lspEditor.connect()

            codeEditor
        },
        modifier = Modifier
            .fillMaxSize(),
        update = {

        }
    )
}