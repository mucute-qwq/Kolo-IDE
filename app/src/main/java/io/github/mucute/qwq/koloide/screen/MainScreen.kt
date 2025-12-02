package io.github.mucute.qwq.koloide.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.mucute.qwq.koloide.R
import io.github.mucute.qwq.koloide.composition.provider.LocalSnackbarHostState
import io.github.mucute.qwq.koloide.viewmodel.MainScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val viewModel: MainScreenViewModel = viewModel()
    val navItems = viewModel.navItems
    val selectedNavItem by viewModel.selectedNavItem.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            )
        },
        floatingActionButton = {
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                AnimatedContent(
                    targetState = selectedNavItem,
                    modifier = Modifier
                        .offset(16.dp, 16.dp)
                ) { navItem ->
                    Column(
                        Modifier
                            .padding(16.dp)
                            .wrapContentSize()
                    ) {
                        navItem.floatingActionButton()
                    }
                }
            }
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
        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
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
}