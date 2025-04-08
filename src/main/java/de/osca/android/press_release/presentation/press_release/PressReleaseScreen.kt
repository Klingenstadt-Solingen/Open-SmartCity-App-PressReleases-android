package de.osca.android.press_release.presentation.press_release

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import de.osca.android.essentials.presentation.component.design.BaseCardContainer
import de.osca.android.essentials.presentation.component.design.BaseTextField
import de.osca.android.essentials.presentation.component.design.MasterDesignArgs
import de.osca.android.essentials.presentation.component.design.RootContainer
import de.osca.android.essentials.presentation.component.screen_wrapper.ScreenWrapper
import de.osca.android.essentials.presentation.component.topbar.ScreenTopBar
import de.osca.android.essentials.utils.extensions.SetSystemStatusBar
import de.osca.android.press_release.presentation.PressReleaseViewModel
import de.osca.android.press_release.presentation.args.PressReleaseListBehaviorArgs
import de.osca.android.press_release.presentation.components.PressReleaseListItem

/**
 * @param placeholderImageId
 * @param isMocked
 */
@Composable
fun PressReleaseScreen(
    navController: NavController,
    isMocked: Boolean = false,
    @DrawableRes placeholderImageId: Int = -1,
    pressReleaseViewModel: PressReleaseViewModel = hiltViewModel(),
    masterDesignArgs: MasterDesignArgs = pressReleaseViewModel.defaultDesignArgs,
    listBehaviorArgs: PressReleaseListBehaviorArgs = PressReleaseListBehaviorArgs()
) {
    val design = pressReleaseViewModel.pressReleaseDesignArgs

    if(!isMocked) {
        LaunchedEffect(Unit) {
            pressReleaseViewModel.initializePressReleases()
        }
    } else {
        pressReleaseViewModel.getMockedPressReleases()
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val scrollState = rememberLazyListState()

    SetSystemStatusBar(
        !(design.mIsStatusBarWhite ?: masterDesignArgs.mIsStatusBarWhite), Color.Transparent
    )

    ScreenWrapper(
        topBar = {
            ScreenTopBar(
                title = stringResource(id = design.vModuleTitle),
                overrideTextColor = design.mTopBarTextColor,
                overrideBackgroundColor = design.mTopBarBackColor,
                navController = navController,
                masterDesignArgs = masterDesignArgs
            )
        },
        navController = navController,
        horizontalAlignment = Alignment.CenterHorizontally,
        screenWrapperState = pressReleaseViewModel.wrapperState,
        modifier = Modifier
            .fillMaxSize(),
        retryAction = {
            if(!isMocked) {
                pressReleaseViewModel.initializePressReleases()
            } else {
                pressReleaseViewModel.getMockedPressReleases()
            }
        },
        masterDesignArgs = masterDesignArgs,
        moduleDesignArgs = design,
    ) {
        RootContainer(
            scrollState = scrollState,
            masterDesignArgs = masterDesignArgs,
            moduleDesignArgs = design
        ) {
            item {
                BaseCardContainer(
                    moduleDesignArgs = design,
                    masterDesignArgs = masterDesignArgs
                ) {
                    BaseTextField(
                        textFieldTitle = "",
                        isError = false,
                        isOutlined = false,
                        onTextChange = {
                            pressReleaseViewModel.search(it)
                        },
                        onClear = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            pressReleaseViewModel.search("")
                        },
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        },
                        masterDesignArgs = masterDesignArgs,
                        moduleDesignArgs = design,
                    )
                }
            }
            items(pressReleaseViewModel.displayedPressReleases) { pressRelease ->
                PressReleaseListItem(
                    navController = navController,
                    pressRelease = pressRelease,
                    design = design,
                    masterDesignArgs = masterDesignArgs,
                    listBehaviorArgs = listBehaviorArgs,
                    placeholderImageId = placeholderImageId
                )
            }
        }
    }
}