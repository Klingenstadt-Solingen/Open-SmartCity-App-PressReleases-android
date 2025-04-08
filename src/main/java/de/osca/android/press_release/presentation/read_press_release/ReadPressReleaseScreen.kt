package de.osca.android.press_release.presentation.read_press_release

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import de.osca.android.essentials.presentation.component.design.*
import de.osca.android.essentials.presentation.component.screen_wrapper.ScreenWrapper
import de.osca.android.essentials.presentation.component.screen_wrapper.ScreenWrapperState
import de.osca.android.essentials.presentation.component.topbar.ScreenTopBar
import de.osca.android.essentials.utils.extensions.SetSystemStatusBar
import de.osca.android.essentials.utils.extensions.shareText
import de.osca.android.press_release.R
import de.osca.android.press_release.presentation.PressReleaseViewModel
import org.matomo.sdk.Tracker
import org.matomo.sdk.extra.TrackHelper

/**
 * @param content
 * @param title
 * @param url
 * @param cityName
 */
@OptIn(ExperimentalCoilApi::class)
@Composable
fun ReadPressReleaseScreen(
    navController: NavController,
    id: String?,
    pressReleaseViewModel: PressReleaseViewModel = hiltViewModel(),
    masterDesignArgs: MasterDesignArgs = pressReleaseViewModel.defaultDesignArgs,
    tracker: Tracker? = null
) {
    val context = LocalContext.current
    val design = pressReleaseViewModel.pressReleaseDesignArgs

    if (tracker != null) {
        TrackHelper
            .track()
            .screen("readPressRelease")
            .title(id)
            .with(tracker)
    }

    val sharePress = remember { mutableStateOf(false) }
    val pressObj = remember { pressReleaseViewModel.foundPressRelease }

    if(id != null) {
        pressReleaseViewModel.fetchPressReleaseById(id)
    }

    if (sharePress.value) {
        sharePress(
            context = context,
            title = pressObj.value?.title ?: "",
            summary = pressObj.value?.summary ?: "",
            appStoreLink = design.appStoreLink,
            url = pressObj.value?.url ?: "",
            cityName = design.cityName,
            sharePress = sharePress
        )
    }

    SetSystemStatusBar(
        !(design.mIsStatusBarWhite ?: masterDesignArgs.mIsStatusBarWhite), Color.Transparent
    )

    ScreenWrapper(
        topBar = {
            ScreenTopBar(
                title = pressObj.value?.title ?: stringResource(id = R.string.press_release_read_title),
                navController = navController,
                overrideTextColor = design.mTopBarTextColor,
                overrideBackgroundColor = design.mTopBarBackColor,
                actions = {
                    IconButton(
                        onClick = {
                            sharePress.value = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            tint = design.mTopBarTextColor ?: masterDesignArgs.mTopBarTextColor,
                            contentDescription = "share"
                        )
                    }
                },
                masterDesignArgs = masterDesignArgs
            )
        },
        screenWrapperState = remember {
            mutableStateOf(ScreenWrapperState.DisplayContent)
        },
        masterDesignArgs = masterDesignArgs,
        moduleDesignArgs = design
    ) {
        RootContainer(
            masterDesignArgs = masterDesignArgs,
            moduleDesignArgs = design
        ) {
            item {
                SimpleSpacedList(
                    masterDesignArgs = masterDesignArgs
                ) {
                    if(pressObj.value?.imageUrl != null && pressObj.value?.imageUrl!!.isNotEmpty()) {
                        BaseCardContainer(
                            masterDesignArgs = masterDesignArgs,
                            moduleDesignArgs = design,
                            useContentPadding = false,
                            overrideConstraintHeight = 200.dp
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(pressObj.value?.imageUrl),
                                contentDescription = "pressImage",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }

                    if(pressObj.value?.content != null) {
                        BaseCardContainer(
                            masterDesignArgs = masterDesignArgs,
                            moduleDesignArgs = design,
                            useContentPadding = false
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Color.White)
                                    .padding(16.dp)
                            ) {
                                Column {
                                    Text(
                                        text = pressObj.value?.title!!,
                                        style = masterDesignArgs.captionTextStyle,
                                        color = design.mCardTextColor
                                            ?: masterDesignArgs.mCardTextColor,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )

                                    if(!pressObj.value?.title.isNullOrEmpty()) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }

                                    HtmlTextWebview(
                                        htmlString = formatHtml(pressObj.value?.content),
                                    )
                                }
                            }
                        }

                        BaseCardContainer(
                            masterDesignArgs = masterDesignArgs,
                            moduleDesignArgs = design
                        ) {
                            OpenWebsiteElement(
                                url = pressObj.value?.url ?: "",
                                context = context,
                                masterDesignArgs = masterDesignArgs,
                                moduleDesignArgs = design
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatHtml(unformattedHtml: String?): String {
    val indexOfStyleEnd = unformattedHtml?.indexOf("</style>") ?: -1

    return if(indexOfStyleEnd > 0) {
        val index = indexOfStyleEnd + 8
        val substring = unformattedHtml?.substring(index, unformattedHtml.length - 1)
        "<html><body>$substring"
    } else {
        unformattedHtml ?: ""
    }
}

@Composable
private fun sharePress(
    context: Context,
    title: String = "",
    url: String = "",
    summary: String = "",
    appStoreLink: String = "",
    @StringRes cityName: Int = -1,
    sharePress: MutableState<Boolean>
) {
    val header = "$title\n\n$summary\n\n${url}"
    val details = "\n\n${stringResource(id = cityName)}-App:\n${appStoreLink}"

    context.shareText(
        title = stringResource(id = R.string.global_share_text),
        text = "$header$details"
    )

    sharePress.value = false
}