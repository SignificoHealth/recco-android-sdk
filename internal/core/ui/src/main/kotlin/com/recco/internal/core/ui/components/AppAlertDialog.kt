package com.recco.internal.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme

@Composable
fun AppAlertDialog(
    openDialog: MutableState<Boolean>,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int? = null,
    @StringRes textButtonPrimaryRes: Int,
    onClickPrimary: () -> Unit,
    onDismiss: () -> Unit = {},
    content: @Composable (ColumnScope.() -> Unit)? = null,
) {
    if (openDialog.value) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {
                openDialog.value = false
                onDismiss()
            }
        ) {
            Box(
                Modifier
                    .padding(AppSpacing.dp_24)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 22.dp)
                        .background(
                            color = AppTheme.colors.background,
                            shape = RoundedCornerShape(AppSpacing.dp_24)
                        )
                ) {
                    header?.invoke(this)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(horizontal = AppSpacing.dp_24)
                    ) {
                        Spacer(Modifier.height(AppSpacing.dp_24))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(titleRes),
                            style = AppTheme.typography.h1,
                            textAlign = TextAlign.Center
                        )

                        descriptionRes?.let {
                            Spacer(Modifier.height(AppSpacing.dp_24))
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(descriptionRes),
                                style = AppTheme.typography.body2,
                                textAlign = TextAlign.Center
                            )
                        }

                        content?.invoke(this)

                        Spacer(Modifier.height(AppSpacing.dp_32))
                        AppPrimaryButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                onClickPrimary()
                                openDialog.value = false
                            },
                            textRes = textButtonPrimaryRes
                        )

                        Spacer(Modifier.height(AppSpacing.dp_32))
                    }
                }
                CloseDialogButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = AppSpacing.dp_24)
                ) {
                    openDialog.value = false
                }
            }
        }
    }
}

@Composable
private fun CloseDialogButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier
            .background(
                color = AppTheme.colors.staticLightGrey,
                shape = CircleShape
            ),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = R.drawable.recco_ic_close),
            tint = AppTheme.colors.staticDark,
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun Preview() {
    val openDialog = remember { mutableStateOf(true) }

    AppTheme {
        AppAlertDialog(
            openDialog = openDialog,
            titleRes = R.string.recco_dashboard_alert_mental_wellbeing_title,
            descriptionRes = R.string.recco_dashboard_alert_mental_wellbeing_body,
            textButtonPrimaryRes = R.string.recco_start,
            onClickPrimary = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    val openDialog = remember { mutableStateOf(true) }

    AppTheme(darkTheme = true) {
        AppAlertDialog(
            openDialog = openDialog,
            titleRes = R.string.recco_dashboard_alert_mental_wellbeing_title,
            descriptionRes = R.string.recco_dashboard_alert_mental_wellbeing_body,
            textButtonPrimaryRes = R.string.recco_start,
            onClickPrimary = {}
        )
    }
}

@Preview
@Composable
private fun PreviewWithHeader() {
    val openDialog = remember { mutableStateOf(true) }

    AppTheme {
        AppAlertDialog(
            openDialog = openDialog,
            header = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppTheme.colors.accent20),
                    contentAlignment = Alignment.Center
                ) {
                    AppTintedImagePeopleDigital(
                        modifier = Modifier.size(237.dp)
                    )
                }
            },
            titleRes = R.string.recco_dashboard_alert_mental_wellbeing_title,
            descriptionRes = R.string.recco_dashboard_alert_mental_wellbeing_body,
            textButtonPrimaryRes = R.string.recco_start,
            onClickPrimary = {}
        )
    }
}

@Preview
@Composable
private fun PreviewWithHeaderDark() {
    val openDialog = remember { mutableStateOf(true) }

    AppTheme(darkTheme = true) {
        AppAlertDialog(
            openDialog = openDialog,
            header = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppTheme.colors.accent20),
                    contentAlignment = Alignment.Center
                ) {
                    AppTintedImagePeopleDigital(
                        modifier = Modifier.size(237.dp)
                    )
                }
            },
            titleRes = R.string.recco_dashboard_alert_mental_wellbeing_title,
            descriptionRes = R.string.recco_dashboard_alert_mental_wellbeing_body,
            textButtonPrimaryRes = R.string.recco_start,
            onClickPrimary = {}
        )
    }
}
