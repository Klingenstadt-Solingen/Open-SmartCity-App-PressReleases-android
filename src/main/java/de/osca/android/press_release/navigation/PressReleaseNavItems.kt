package de.osca.android.press_release.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import de.osca.android.essentials.domain.entity.navigation.NavigationItem
import de.osca.android.press_release.R
import de.osca.android.press_release.domain.entity.PressRelease

/**
 * Defines default routes of the Press Release module
 */
sealed class PressReleaseNavItems {

    object PressReleaseNavItem : NavigationItem(
        title = R.string.press_release_title,
        route = PRESS_RELEASE_ROUTE,
        icon = R.drawable.ic_circle,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "solingen://$PRESS_RELEASE_ROUTE"
            }
        ),
    )

    object ReadPressReleaseNavItem : NavigationItem(
        title = R.string.press_release_read_title,
        route = getPressReleaseRoute(),
        arguments = listOf(
            navArgument(ARG_PRESS_RELEASE_ID) {
                type = NavType.StringType
            }
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "solingen://$READ_PRESS_RELEASE_ROUTE?$ARG_PRESS_RELEASE_ID={$ARG_PRESS_RELEASE_ID}"
            }
        ),
        icon = R.drawable.ic_circle
    )

    companion object {
        const val ARG_PRESS_RELEASE_ID = "object"

        const val PRESS_RELEASE_ROUTE = "pressreleases"
        const val READ_PRESS_RELEASE_ROUTE = "pressreleases/detail"

        fun getPressReleaseRoute(pressRelease: PressRelease? = null): String {
            return when (pressRelease) {
                null -> "$READ_PRESS_RELEASE_ROUTE?$ARG_PRESS_RELEASE_ID={$ARG_PRESS_RELEASE_ID}"
                else -> "$READ_PRESS_RELEASE_ROUTE?$ARG_PRESS_RELEASE_ID=${pressRelease.objectId}"
            }
        }
    }
}