package io.github.mucute.qwq.koloide.page.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import io.github.mucute.qwq.koloide.component.dividerPreference
import io.github.mucute.qwq.koloide.component.headerPreference
import io.github.mucute.qwq.koloide.preference.Preference
import io.github.mucute.qwq.koloide.R
import io.github.mucute.qwq.koloide.composition.provider.LocalNavController
import io.github.mucute.qwq.koloide.navigation.NavScreen
import me.zhanghai.compose.preference.preference

@Composable
fun SettingPage() {
    val navController = LocalNavController.current
    Column(Modifier.fillMaxSize()) {
        LazyColumn {
            generalPreferences()
            modulesPreferences()
            developerOptionsPreferences(navController)
            aboutPreferences()
        }
    }
}

private fun LazyListScope.generalPreferences() {
    headerPreference(
        key = Preference.Main.SettingPage.GeneralHeader,
        summary = { Text(stringResource(R.string.general)) }
    )
    preference(
        key = Preference.Main.SettingPage.Theme,
        title = { Text(stringResource(R.string.theme)) },
        summary = { Text(stringResource(R.string.theme_summary)) },
        onClick = {}
    )
    preference(
        key = Preference.Main.SettingPage.General,
        title = { Text(stringResource(R.string.general)) },
        summary = { Text(stringResource(R.string.general_summary)) },
        onClick = {}
    )
    preference(
        key = Preference.Main.SettingPage.Editor,
        title = { Text(stringResource(R.string.editor)) },
        summary = { Text(stringResource(R.string.editor_summary)) },
        onClick = {}
    )
    preference(
        key = Preference.Main.SettingPage.BuildAndRun,
        title = { Text(stringResource(R.string.build_and_run)) },
        summary = { Text(stringResource(R.string.build_and_run_summary)) },
        onClick = {}
    )
    dividerPreference()
}

private fun LazyListScope.modulesPreferences() {
    headerPreference(
        key = Preference.Main.SettingPage.ModulesHeader,
        summary = { Text(stringResource(R.string.module)) }
    )
    preference(
        key = Preference.Main.SettingPage.ModulesManagement,
        title = { Text(stringResource(R.string.modules_management)) },
        summary = { Text(stringResource(R.string.modules_management_summary)) },
        onClick = {}
    )
    dividerPreference()
}

private fun LazyListScope.developerOptionsPreferences(navController: NavController) {
    headerPreference(
        key = Preference.Main.SettingPage.DeveloperOptionsHeader,
        summary = { Text(stringResource(R.string.developer_options)) }
    )
    preference(
        key = Preference.Main.SettingPage.Terminal,
        title = { Text(stringResource(R.string.terminal)) },
        summary = { Text(stringResource(R.string.terminal_summary)) },
        onClick = {
            navController.navigate(NavScreen.Terminal)
        }
    )
    dividerPreference()
}

private fun LazyListScope.aboutPreferences() {
    headerPreference(
        key = Preference.Main.SettingPage.AboutHeader,
        summary = { Text(stringResource(R.string.about)) }
    )
    preference(
        key = Preference.Main.SettingPage.About,
        title = { Text(stringResource(R.string.about)) },
        summary = { Text(stringResource(R.string.about_summary)) },
        onClick = {}
    )
}