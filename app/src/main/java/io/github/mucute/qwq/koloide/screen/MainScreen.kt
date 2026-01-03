package io.github.mucute.qwq.koloide.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Book
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material.icons.twotone.Token
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.mucute.qwq.koloide.R
import io.github.mucute.qwq.koloide.composition.provider.LocalSnackbarHostState
import io.github.mucute.qwq.koloide.model.NavItem
import io.github.mucute.qwq.koloide.page.main.DocumentPage
import io.github.mucute.qwq.koloide.page.main.HomePage
import io.github.mucute.qwq.koloide.page.main.ModulePage
import io.github.mucute.qwq.koloide.page.main.ModulePageFloatingActionButton
import io.github.mucute.qwq.koloide.page.main.SettingPage

private val navItems = listOf(
    NavItem(
        icon = Icons.TwoTone.Home,
        labelResId = R.string.home,
        content = { HomePage() },
    ),
    NavItem(
        icon = Icons.TwoTone.Token,
        labelResId = R.string.module,
        content = { ModulePage() },
        floatingActionButton = { ModulePageFloatingActionButton() }
    ),
    NavItem(
        icon = Icons.TwoTone.Book,
        labelResId = R.string.document,
        content = { DocumentPage() }
    ),
    NavItem(
        icon = Icons.TwoTone.Settings,
        labelResId = R.string.setting,
        content = { SettingPage() }
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedNavItem by retain { mutableStateOf(navItems.first()) }
    val snackbarHostState = retain { SnackbarHostState() }

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
                            selectedNavItem = navItem
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