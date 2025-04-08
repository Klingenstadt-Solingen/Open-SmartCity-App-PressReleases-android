package de.osca.android.press_release.presentation.components

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import de.osca.android.essentials.presentation.component.design.BaseCardContainer
import de.osca.android.essentials.presentation.component.design.MasterDesignArgs
import de.osca.android.essentials.utils.extensions.toDateString
import de.osca.android.press_release.R
import de.osca.android.press_release.domain.entity.PressRelease
import de.osca.android.press_release.navigation.PressReleaseNavItems
import de.osca.android.press_release.presentation.args.PressReleaseDesignArgs
import de.osca.android.press_release.presentation.args.PressReleaseListBehaviorArgs

@Composable
fun PressReleaseListItem(
    navController: NavController,
    pressRelease: PressRelease,
    design: PressReleaseDesignArgs,
    masterDesignArgs: MasterDesignArgs,
    listBehaviorArgs: PressReleaseListBehaviorArgs,
    @DrawableRes placeholderImageId: Int = -1
) {
    val readingTime = stringResource(id = R.string.press_release_reading_time, pressRelease.readingTime ?: 0)

    fun onItemClicked() {
        when (listBehaviorArgs.onPressReleaseClicked) {
            null -> openPressReleaseInWebview(
                pressRelease = pressRelease,
                navController = navController
            )
            else -> listBehaviorArgs.onPressReleaseClicked.invoke(pressRelease)
        }
    }

    BaseCardContainer(
        onClick = {
            onItemClicked()
        },
        useContentPadding = false,
        moduleDesignArgs = design,
        masterDesignArgs = masterDesignArgs
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(design.showPressImage) {
                Card(modifier = Modifier
                        .size(100.dp),
                    shape = RoundedCornerShape(0.dp),
                    backgroundColor = if (placeholderImageId >= 0)
                        masterDesignArgs.appSpecificColor
                    else
                        (design.mCardBackColor ?: masterDesignArgs.mCardBackColor),
                    elevation = 0.dp
                ) {
                    // ToDo: same as Service (errorImage)
                    Image(
                        painter = if(!pressRelease.imageUrl.isNullOrEmpty()) {
                            rememberAsyncImagePainter(pressRelease.imageUrl)
                        } else if (placeholderImageId >= 0) {
                            painterResource(id = placeholderImageId)
                        } else {
                            painterResource(id = R.drawable.ic_circle)
                        },
                        contentDescription = "pressReleaseImage",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = design.mCardContentPadding ?: masterDesignArgs.mCardContentPadding)
            ) {
                if(pressRelease.category != null) {
                    Text(
                        text = pressRelease.category,
                        style = masterDesignArgs.subtitleBoldTextStyle,
                        color = design.mCardTextColor ?: masterDesignArgs.mCardTextColor,
                        maxLines = 1
                    )
                }

                Text(
                    text = pressRelease.title ?: "",
                    style = masterDesignArgs.overlineTextStyle,
                    color = design.mCardTextColor ?: masterDesignArgs.mCardTextColor,
                    maxLines = 2
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if(design.showClockIcon) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clock),
                            tint = masterDesignArgs.highlightColor,
                            contentDescription = stringResource(id = R.string.press_release_posted_at),
                            modifier = Modifier
                                .size(15.dp)
                                .padding(end = 4.dp)
                        )
                    }
                    if(design.showDate || design.showReadingTime) {
                        Text(
                            text = "${if(design.showDate) pressRelease.releaseDate.toDateString() ?: "" else ""} ${if(design.showDate && design.showReadingTime) " - " else ""} ${if(design.showReadingTime) readingTime else ""}",
                            style = masterDesignArgs.normalTextStyle,
                            color = design.mCardTextColor ?: masterDesignArgs.mCardTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

private fun openPressReleaseInWebview(pressRelease: PressRelease, navController: NavController) {
    when (pressRelease.url) {
        null ->  navController.navigate(
            PressReleaseNavItems.getPressReleaseRoute(pressRelease)
        )
        else -> navController.navigate(
            PressReleaseNavItems.getPressReleaseRoute(pressRelease)
        )
    }
}