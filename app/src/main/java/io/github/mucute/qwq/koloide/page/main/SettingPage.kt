package io.github.mucute.qwq.koloide.page.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.mucute.qwq.koloide.component.dividerPreference
import io.github.mucute.qwq.koloide.component.headerPreference
import io.github.mucute.qwq.koloide.preference.Preference
import io.githun.mucute.qwq.koloide.R
import io.githun.mucute.qwq.koloide.component.dividerPreference
import io.githun.mucute.qwq.koloide.component.headerPreference
import io.githun.mucute.qwq.koloide.preference.Preference
import me.zhanghai.compose.preference.preference

@Composable
fun SettingPage() {
    Column(Modifier.fillMaxSize()) {
        LazyColumn {
            generalPreferences()
            extensionsPreferences()
            developerOptionsPreferences()
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

private fun LazyListScope.extensionsPreferences() {
    headerPreference(
        key = Preference.Main.SettingPage.ExtensionsHeader,
        summary = { Text(stringResource(R.string.extension)) }
    )
    preference(
        key = Preference.Main.SettingPage.ExtensionsManagement,
        title = { Text(stringResource(R.string.extensions_management)) },
        summary = { Text(stringResource(R.string.extensions_management_summary)) },
        onClick = {}
    )
    dividerPreference()
}

private fun LazyListScope.developerOptionsPreferences() {
    headerPreference(
        key = Preference.Main.SettingPage.DeveloperOptionsHeader,
        summary = { Text(stringResource(R.string.developer_options)) }
    )
    preference(
        key = Preference.Main.SettingPage.DeveloperOptions,
        title = { Text(stringResource(R.string.developer_options)) },
        summary = { Text(stringResource(R.string.developer_options_summary)) },
        onClick = {}
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