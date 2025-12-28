package io.github.mucute.qwq.koloide.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Book
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material.icons.twotone.Token
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.mucute.qwq.koloide.R
import io.github.mucute.qwq.koloide.model.NavItem
import io.github.mucute.qwq.koloide.model.SelectableCardDropDownMenuItem
import io.github.mucute.qwq.koloide.page.main.DocumentPage
import io.github.mucute.qwq.koloide.page.main.ModulePage
import io.github.mucute.qwq.koloide.page.main.HomePage
import io.github.mucute.qwq.koloide.page.main.ModulePageFloatingActionButton
import io.github.mucute.qwq.koloide.page.main.SettingPage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {

    val navItems = listOf(
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

    val projectCardDropDownMenuItems = listOf(
        SelectableCardDropDownMenuItem(
            leadingIcon = Icons.TwoTone.Edit,
            textResId = R.string.rename
        ),
        SelectableCardDropDownMenuItem(
            leadingIcon = Icons.TwoTone.Delete,
            textResId = R.string.delete
        ),
    )

    val moduleCardDropDownMenuItems = listOf(
        SelectableCardDropDownMenuItem(
            leadingIcon = Icons.TwoTone.Delete,
            textResId = R.string.uninstall
        )
    )

    private val _selectedNavItem = MutableStateFlow(navItems.first())

    val selectedNavItem = _selectedNavItem.asStateFlow()

    fun selectNavItem(navItem: NavItem) {
        viewModelScope.launch {
            _selectedNavItem.update { navItem }
        }
    }

}