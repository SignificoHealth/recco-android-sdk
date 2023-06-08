package com.shadowflight.core.ui.components

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shadowflight.core.ui.R
import com.shadowflight.core.ui.theme.AppSpacing
import com.shadowflight.core.ui.theme.AppTheme

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleAlign: TextAlign = TextAlign.Start,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = { CloseIconButton() },
    backgroundColor: Color = AppTheme.colors.background,
    elevation: Dp = AppBarDefaults.TopAppBarElevation
) {
    Surface(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth(),
        color = backgroundColor,
        elevation = elevation
    ) {
        val layoutHeight = with(LocalDensity.current) {
            AppTopBarDefaults.Height.roundToPx()
        }

        Layout(
            modifier = modifier,
            content = {
                Box(
                    modifier = Modifier
                        .layoutId("navigationIcon")
                        .padding(start = 4.dp)
                ) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides 1f, content = navigationIcon
                    )
                }
                Box(
                    modifier = Modifier
                        .layoutId("title")
                        .padding(horizontal = 4.dp)
                ) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides 1f,
                        content = @Composable {
                            Text(
                                modifier = Modifier.padding(horizontal = AppSpacing.dp_8),
                                text = title.orEmpty(),
                                style = AppTheme.typography.h4,
                                textAlign = titleAlign,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        })
                }
                Box(
                    modifier = Modifier
                        .layoutId("actions")
                        .padding(end = 4.dp)
                ) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides 1f,
                        content = @Composable {
                            Row(
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically,
                                content = actions
                            )
                        })
                }
            },
        ) { measurables, constraints ->
            val navigationIconPlaceable = measurables.first { it.layoutId == "navigationIcon" }
                .measure(constraints.copy(minWidth = 0))
            val actionsPlaceable = measurables.first { it.layoutId == "actions" }
                .measure(constraints.copy(minWidth = 0))

            val maxTitleWidth = constraints.maxWidth.minus(navigationIconPlaceable.width)
                .minus(actionsPlaceable.width.coerceAtLeast(navigationIconPlaceable.width))
                .coerceAtLeast(0)
            val titlePlaceable = measurables.first { it.layoutId == "title" }
                .measure(constraints.copy(minWidth = 0, maxWidth = maxTitleWidth))

            layout(constraints.maxWidth, layoutHeight) {
                // Navigation icon
                navigationIconPlaceable.placeRelative(
                    x = 0,
                    y = (layoutHeight - navigationIconPlaceable.height) / 2
                )

                // Title
                titlePlaceable.placeRelative(
                    x = (constraints.maxWidth - titlePlaceable.width) / 2,
                    y = (layoutHeight - titlePlaceable.height) / 2
                )

                // Actions
                actionsPlaceable.placeRelative(
                    x = constraints.maxWidth - actionsPlaceable.width,
                    y = (layoutHeight - actionsPlaceable.height) / 2
                )
            }
        }
    }
}

@Composable
fun BackIconButton(
    modifier: Modifier = Modifier,
    iconTint: Color = AppTheme.colors.primary,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            tint = iconTint,
            contentDescription = null,
        )
    }
}

@Composable
fun CloseIconButton(
    modifier: Modifier = Modifier,
    iconTint: Color = AppTheme.colors.primary,
    onClick: (context: Context) -> Unit = { (it as AppCompatActivity).finish() },
) {
    val context = LocalContext.current

    IconButton(
        modifier = modifier,
        onClick = { onClick(context) },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_close),
            tint = iconTint,
            contentDescription = null,
        )
    }
}

object AppTopBarDefaults {
    val Height = 56.dp
}

@Preview
@Composable
private fun PreviewWithTitle() {
    AppTheme {
        AppTopBar(title = "Title")
    }
}

@Preview
@Composable
private fun PreviewWithLongTitle() {
    AppTheme {
        AppTopBar(
            title = "Lorem ipsum dolor sit amet consectetur adipiscing elit",
        )
    }
}

@Preview
@Composable
private fun PreviewWithBackIcon() {
    AppTopBar(
        title = "Title",
        navigationIcon = {
            BackIconButton {}
        }
    )
}

@Preview
@Composable
private fun PreviewWithBackIconAndLongTitle() {
    AppTheme {
        AppTopBar(
            title = "Lorem ipsum dolor sit amet consectetur adipiscing elit",
            navigationIcon = {
                BackIconButton {}
            }
        )
    }
}

@Preview
@Composable
private fun PreviewWithBackAndActionIcons() {
    AppTopBar(
        title = "Title",
        navigationIcon = {
            BackIconButton {}
        },
        actions = {
            CloseIconButton(onClick = {})
        }
    )
}

@Preview
@Composable
private fun PreviewWithBackAndActionIconsAndLongTitle() {
    AppTopBar(
        title = "Lorem ipsum dolor sit amet consectetur adipiscing elit",
        navigationIcon = {
            BackIconButton {}
        },
        actions = {
            CloseIconButton(onClick = {})
        }
    )
}
