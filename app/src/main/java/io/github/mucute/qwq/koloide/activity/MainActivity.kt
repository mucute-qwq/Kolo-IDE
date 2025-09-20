package io.github.mucute.qwq.koloide.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import io.github.mucute.qwq.koloide.navigation.Navigation
import io.github.mucute.qwq.koloide.shared.activity.BaseActivity
import io.github.mucute.qwq.koloide.shared.ui.theme.KoloIDETheme

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoloIDETheme {
                Navigation()
            }
        }
    }

}