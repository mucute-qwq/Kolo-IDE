package io.github.mucute.qwq.koloide.preference

@Suppress("ConstPropertyName")
sealed interface Preference {

    sealed interface Main : Preference {

        object SettingPage : Main {

            const val GeneralHeader = "general_header"

            const val Theme = "theme"

            const val General = "general"

            const val Editor = "editor"

            const val BuildAndRun = "build_and_run"

            const val ExtensionsHeader = "extensions_header"

            const val ExtensionsManagement = "extensions_management"

            const val DeveloperOptionsHeader = "developer_options_header"

            const val DeveloperOptions = "developer_options"

            const val AboutHeader = "about_header"

            const val About = "about"

        }

    }

}