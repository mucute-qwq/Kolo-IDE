package io.github.mucute.qwq.koloide.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Book
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.Extension
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.githun.mucute.qwq.koloide.R
import io.githun.mucute.qwq.koloide.component.SelectableCardDropDownMenu
import io.githun.mucute.qwq.koloide.model.NavItem
import io.githun.mucute.qwq.koloide.model.SelectableCardDropDownMenuItem
import io.githun.mucute.qwq.koloide.page.main.DocumentPage
import io.githun.mucute.qwq.koloide.page.main.ExtensionPage
import io.githun.mucute.qwq.koloide.page.main.HomePage
import io.github.mucute.qwq.koloide.page.main.SettingPage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {

    val navItems = listOf(
        _root_ide_package_.io.github.mucute.qwq.koloide.model.NavItem(
            icon = Icons.TwoTone.Home,
            labelResId = R.string.home,
            content = { _root_ide_package_.io.github.mucute.qwq.koloide.page.main.HomePage() }
        ),
        _root_ide_package_.io.github.mucute.qwq.koloide.model.NavItem(
            icon = Icons.TwoTone.Extension,
            labelResId = R.string.extension,
            content = { _root_ide_package_.io.github.mucute.qwq.koloide.page.main.ExtensionPage() }
        ),
        _root_ide_package_.io.github.mucute.qwq.koloide.model.NavItem(
            icon = Icons.TwoTone.Book,
            labelResId = R.string.document,
            content = { _root_ide_package_.io.github.mucute.qwq.koloide.page.main.DocumentPage() }
        ),
        _root_ide_package_.io.github.mucute.qwq.koloide.model.NavItem(
            icon = Icons.TwoTone.Settings,
            labelResId = R.string.setting,
            content = { SettingPage() }
        )
    )

    val projectCardDropDownMenuItems = listOf(
        _root_ide_package_.io.github.mucute.qwq.koloide.model.SelectableCardDropDownMenuItem(
            leadingIcon = Icons.TwoTone.Edit,
            textResId = R.string.rename
        ),
        _root_ide_package_.io.github.mucute.qwq.koloide.model.SelectableCardDropDownMenuItem(
            leadingIcon = Icons.TwoTone.Delete,
            textResId = R.string.delete
        ),
    )

    val extensionCardDropDownMenuItems = listOf(
        _root_ide_package_.io.github.mucute.qwq.koloide.model.SelectableCardDropDownMenuItem(
            leadingIcon = Icons.TwoTone.Delete,
            textResId = R.string.delete
        )
    )

    private val _selectedNavItem = MutableStateFlow(navItems.first())

    val selectedNavItem = _selectedNavItem.asStateFlow()

    fun selectNavItem(navItem: io.github.mucute.qwq.koloide.model.NavItem) {
        viewModelScope.launch {
            _selectedNavItem.update { navItem }
        }
    }

}