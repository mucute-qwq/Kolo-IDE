package io.github.mucute.qwq.koloide.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.mucute.qwq.koloide.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val viewModel: io.github.mucute.qwq.koloide.viewmodel.MainScreenViewModel = viewModel()
    val navItems = viewModel.navItems
    val selectedNavItem by viewModel.selectedNavItem.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                }
            )
        },
        bottomBar = {
            NavigationBar {
                navItems.forEach { navItem ->
                    NavigationBarItem(
                        selected = navItem === selectedNavItem,
                        onClick = {
                            viewModel.selectNavItem(navItem)
                        },
                        icon = {
                            Icon(navItem.icon, contentDescription = null)
                        },
                        label = {
                            Text(stringResource(navItem.labelResId))
                        },
                        alwaysShowLabel = false
                    )
                }
            }
        }
    ) {
        Column(Modifier.padding(it)) {
            AnimatedContent(
                targetState = selectedNavItem,
                modifier = Modifier
                    .fillMaxSize()
            ) { navItem ->
                navItem.content()
            }
        }
    }
}