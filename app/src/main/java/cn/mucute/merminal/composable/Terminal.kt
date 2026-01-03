package cn.mucute.merminal.composable

import android.graphics.Typeface
import android.view.KeyEvent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import cn.mucute.merminal.view.ShellTermSession
import cn.mucute.merminal.view.TermSessionCallback
import cn.mucute.merminal.view.TermViewClient
import cn.mucute.merminal.view.TerminalView

@Composable
fun Terminal(
    modifier: Modifier = Modifier,
    sessionController: SessionController,
    colorScheme: TerminalColorScheme = TerminalDefaults.terminalColors(),
    onSessionFinished: () -> Unit = {}
) {
    val context = LocalContext.current
    val terminalView = remember {
        TerminalView(context).apply {
            isFocusable = true
            isFocusableInTouchMode = true
            setTypeface(Typeface.createFromAsset(context.assets, "font/JetBrainsMono-Regular.ttf"))
        }
    }
    val shortcutKeyController = remember { ShortcutKeyController() }
    val sessionCallback = remember { TermSessionCallback(terminalView, onSessionFinished) }
    val session = remember { sessionController.create(sessionCallback) }

    Column(
        modifier
            .safeContentPadding()
    ) {
        TerminalImpl(
            terminalView = terminalView,
            colorScheme = colorScheme,
            session = session,
            shortcutKeyController = shortcutKeyController,
            modifier = Modifier
                .weight(1f)
        )
        HorizontalDivider()
        ShortcutKey(
            shortcutKeyController = shortcutKeyController,
            onPressKey = {
                terminalView.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, it))
                terminalView.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, it))
            },
            onWriteSymbol = {
                session.write(it)
            },
            onKill = {
                terminalView.currentSession.finishIfRunning()
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}

@Composable
private fun TerminalImpl(
    terminalView: TerminalView,
    colorScheme: TerminalColorScheme,
    session: ShellTermSession,
    shortcutKeyController: ShortcutKeyController,
    modifier: Modifier,
) {
    AndroidView(
        factory = {
            terminalView
        },
        modifier = modifier,
        update = { terminalView ->
            terminalView.setBackgroundColor(colorScheme.background.toArgb())

            val viewClient =
                TermViewClient(terminalView, session, shortcutKeyController)
            session.colorScheme = (
                    cn.mucute.merminal.core.TerminalColorScheme().apply {
                        updateWith(
                            colorScheme.text.toArgb(),
                            colorScheme.background.toArgb(),
                            colorScheme.cursor.toArgb(),
                            mutableMapOf()
                        )
                    })
            terminalView.setEnableWordBasedIme(false)
            terminalView.setTerminalViewClient(viewClient)
            terminalView.attachSession(session)
        }
    )
}

@Composable
fun rememberSessionController(
    command: String? = null,
    currentWorkingDirectory: String,
    environment: MutableMap<String, String> = systemEnvironment(),
) = remember(command, currentWorkingDirectory, environment) {
    SessionController(
        command,
        currentWorkingDirectory,
        environment
    )
}

fun systemEnvironment() = mutableMapOf(
    "TERM" to "xterm-256color",
    "ANDROID_ROOT" to System.getenv("ANDROID_ROOT")!!,
    "ANDROID_DATA" to System.getenv("ANDROID_DATA")!!,
    "COLORTERM" to "truecolor",
)