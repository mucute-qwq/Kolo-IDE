package io.githun.mucute.qwq.koloide.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import io.githun.mucute.qwq.koloide.navigation.Navigation
import io.githun.mucute.qwq.koloide.shared.ui.theme.KoloIDETheme

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