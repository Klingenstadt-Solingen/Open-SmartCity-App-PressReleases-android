package de.osca.android.press_release.presentation.args

import de.osca.android.essentials.presentation.component.design.ModuleDesignArgs
import de.osca.android.essentials.presentation.component.design.WidgetDesignArgs

interface PressReleaseDesignArgs : ModuleDesignArgs, WidgetDesignArgs {
    val cityName: Int
    val appStoreLink: String
    val showPressImage: Boolean
    val showClockIcon: Boolean
    val showDate: Boolean
    val showReadingTime: Boolean
    val previewCountForWidget: Int
}