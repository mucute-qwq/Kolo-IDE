package io.github.mucute.qwq.koloide.activity

import android.graphics.Typeface
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import io.github.mucute.qwq.koloide.R
import io.github.mucute.qwq.koloide.lsp.ProcessStreamConnectionProvider
import io.github.mucute.qwq.koloide.shared.activity.BaseActivity
import io.github.rosemoe.sora.langs.monarch.MonarchColorScheme
import io.github.rosemoe.sora.langs.monarch.MonarchLanguage
import io.github.rosemoe.sora.langs.monarch.registry.ThemeRegistry
import io.github.rosemoe.sora.lsp.client.languageserver.serverdefinition.CustomLanguageServerDefinition
import io.github.rosemoe.sora.lsp.client.languageserver.serverdefinition.CustomLanguageServerDefinition.ServerConnectProvider
import io.github.rosemoe.sora.lsp.editor.LspEditor
import io.github.rosemoe.sora.lsp.editor.LspProject
import io.github.rosemoe.sora.widget.CodeEditor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.eclipse.lsp4j.DidChangeWorkspaceFoldersParams
import org.eclipse.lsp4j.WorkspaceFolder
import org.eclipse.lsp4j.WorkspaceFoldersChangeEvent
import kotlin.concurrent.thread

class LspTestActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lsp_test)

        val codeEditor = findViewById<CodeEditor>(R.id.codeEditor)
        setupCodeEditor(codeEditor)
        createTestFiles()
        connectToLsp(codeEditor)

    }

    private fun setupCodeEditor(codeEditor: CodeEditor) {
        val typeface = Typeface.createFromAsset(assets, "font/JetBrainsMono-Regular.ttf")
        codeEditor.typefaceLineNumber = typeface
        codeEditor.typefaceText = typeface
        codeEditor.colorScheme = MonarchColorScheme.create(ThemeRegistry.currentTheme)
        codeEditor.setEditorLanguage(MonarchLanguage.create("source.js", true))
    }

    private fun createTestFiles() {
        val projectFolder = filesDir.resolve("test")
        projectFolder.mkdirs()

        val packageFile = projectFolder.resolve("package.json")
        packageFile.writeText(
            """
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
            """.trimIndent()
        )

        val mainFile = projectFolder.resolve("index.js")
        mainFile.createNewFile()
    }

    private fun connectToLsp(codeEditor: CodeEditor) {
        scope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                toast("(Kotlin Activity) Starting Language Server...")
                codeEditor.editable = false
            }

            val projectFolder = filesDir.resolve("test")
            val mainFile = projectFolder.resolve("index.js")

            val serverDefinition =
                object : CustomLanguageServerDefinition(
                    "js",
                    ServerConnectProvider {
                        ProcessStreamConnectionProvider()
                    }
                ) {}

            val lspProject = LspProject(projectFolder.absolutePath)
            lspProject.addServerDefinition(serverDefinition)

            var lspEditor: LspEditor

            withContext(Dispatchers.Main) {
                lspEditor = lspProject.createEditor(mainFile.absolutePath)
                lspEditor.wrapperLanguage = codeEditor.editorLanguage
                lspEditor.editor = codeEditor
                lspEditor.isEnableInlayHint = true
            }

            var connected: Boolean

            try {
                lspEditor.connectWithTimeout()
                lspEditor.requestManager?.didChangeWorkspaceFolders(
                    DidChangeWorkspaceFoldersParams().apply {
                        this.event = WorkspaceFoldersChangeEvent().apply {
                            added =
                                listOf(
                                    WorkspaceFolder(
                                        "file://${projectFolder.absolutePath}",
                                        "test"
                                    )
                                )
                        }
                    }
                )

                connected = true

            } catch (e: Exception) {
                connected = false
                e.printStackTrace()
            }

            lifecycleScope.launch(Dispatchers.Main) {
                if (connected) {
                    toast("Initialized Language server")
                } else {
                    toast("Unable to connect language server")
                }
                codeEditor.editable = true
            }
        }
    }

    private fun toast(text: String) {
        Toast.makeText(
            this,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

}