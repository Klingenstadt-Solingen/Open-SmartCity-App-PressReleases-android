package de.osca.android.press_release.presentation.args

import de.osca.android.press_release.domain.entity.PressRelease

data class PressReleaseListBehaviorArgs(
    val onPressReleaseClicked: ((PressRelease) -> Unit)? = null
)