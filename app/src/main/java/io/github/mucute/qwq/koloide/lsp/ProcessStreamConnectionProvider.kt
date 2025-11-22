package io.github.mucute.qwq.koloide.lsp

import io.github.mucute.qwq.koloide.application.AppContext
import io.github.rosemoe.sora.lsp.client.connection.StreamConnectionProvider
import java.io.InputStream
import java.io.OutputStream

class ProcessStreamConnectionProvider : StreamConnectionProvider {

    private val processBuilder = ProcessBuilder("/system/bin/sh")
        .redirectErrorStream(true)

    private lateinit var process: Process

    override val inputStream: InputStream
        get() = process.inputStream

    override val outputStream: OutputStream
        get() = process.outputStream

    override fun start() {
        process = processBuilder.start()

        val stdin = process.outputStream

        stdin.write($$"export PATH=$PATH:$${AppContext.instance.filesDir.resolve("usr/bin").absolutePath}\n".toByteArray())
        stdin.flush()

        stdin.write("typescript-language-server --stdio\n".toByteArray())
        stdin.flush()

    }

    override fun close() {
        process.destroy()
    }

}