package de.osca.android.press_release.widget

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import de.osca.android.essentials.presentation.component.design.BaseListContainer
import de.osca.android.essentials.presentation.component.design.MasterDesignArgs
import de.osca.android.essentials.utils.extensions.safeTake
import de.osca.android.press_release.domain.entity.PressRelease
import de.osca.android.press_release.navigation.PressReleaseNavItems
import de.osca.android.press_release.presentation.PressReleaseViewModel
import de.osca.android.press_release.presentation.args.PressReleaseListBehaviorArgs
import de.osca.android.press_release.presentation.components.PressReleaseListItem

/**
 *
 */
@Composable
fun PressReleaseWidget(
    navController: NavController,
    isMocked: Boolean = false,
    onlyCategory: String? = null,
    secondCategory: String? = null,
    @DrawableRes placeholderImageId: Int = -1,
    @DrawableRes iconUnderLine: Int = -1,
    underLineColor: Color = Color.White,
    listBehaviorArgs: PressReleaseListBehaviorArgs = PressReleaseListBehaviorArgs(),
    pressReleaseViewModel: PressReleaseViewModel = hiltViewModel(),
    masterDesignArgs: MasterDesignArgs = pressReleaseViewModel.defaultDesignArgs
) {
    if(pressReleaseViewModel.pressReleaseDesignArgs.vIsWidgetVisible) {
        val design = pressReleaseViewModel.pressReleaseDesignArgs

        if(!isMocked) {
            LaunchedEffect(Unit) {
                pressReleaseViewModel.initializePressReleases()
            }
        } else {
            pressReleaseViewModel.getMockedPressReleases()
        }

        BaseListContainer(
            text = stringResource(id = design.vWidgetTitle),
            showMoreOption = design.vWidgetShowMoreOption,
            iconUnderLine = iconUnderLine,
            underLineColor = underLineColor,
            moduleDesignArgs = design,
            onMoreOptionClick = {
                navController.navigate(PressReleaseNavItems.PressReleaseNavItem.route)
            },
            masterDesignArgs = masterDesignArgs
        ) {
            val displayList = mutableListOf<PressRelease>()

            if(onlyCategory != null) {
                pressReleaseViewModel.displayedPressReleases.filter { it.category == onlyCategory }.safeTake(pressReleaseViewModel.pressReleaseDesignArgs.previewCountForWidget).forEach { pressRelease ->
                    displayList.add(pressRelease)
                }
                if (secondCategory != null) {
                    pressReleaseViewModel.displayedPressReleases.filter { it.category == secondCategory }.safeTake(pressReleaseViewModel.pressReleaseDesignArgs.previewCountForWidget).forEach { pressRelease ->
                        displayList.add(pressRelease)
                    }
                }
            } else {
                pressReleaseViewModel.displayedPressReleases.safeTake(pressReleaseViewModel.pressReleaseDesignArgs.previewCountForWidget).forEach { pressRelease ->
                    displayList.add(pressRelease)
                }
            }

            displayList.forEach {
                PressReleaseListItem(
                    navController = navController,
                    pressRelease = it,
                    design = design,
                    masterDesignArgs = masterDesignArgs,
                    listBehaviorArgs = listBehaviorArgs,
                    placeholderImageId = placeholderImageId
                )
            }
        }
    }
}