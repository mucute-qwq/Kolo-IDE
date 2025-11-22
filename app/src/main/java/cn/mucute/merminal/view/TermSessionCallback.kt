package cn.mucute.merminal.view

import android.content.Context
import android.view.inputmethod.InputMethodManager
import cn.mucute.merminal.core.TerminalSession

class TermSessionCallback(
    private val terminalView: TerminalView,
    private val onSessionFinished: () -> Unit
) : TerminalSession.SessionChangedCallback {

    override fun onTextChanged(changedSession: TerminalSession?) {
        terminalView.onScreenUpdated()
    }

    override fun onTitleChanged(changedSession: TerminalSession?) {

    }

    override fun onSessionFinished(finishedSession: TerminalSession?) {
        (terminalView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(
                terminalView.windowToken,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        onSessionFinished()
    }

    override fun onClipboardText(session: TerminalSession?, text: String?) {

    }

    override fun onColorsChanged(session: TerminalSession?) {
        terminalView.onScreenUpdated()
    }
}
