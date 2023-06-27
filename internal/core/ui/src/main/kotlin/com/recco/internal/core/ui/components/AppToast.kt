package com.recco.internal.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.extensions.noRippleClickable
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme
import com.recco.internal.core.ui.theme.elevation
import java.util.UUID

enum class ToastType {
    Reward, Confirmation, Error
}

@Composable
fun GlobalToastEvent(
    id: UUID = UUID.randomUUID(),
    title: String,
    description: String?,
    type: ToastType
) {

    // Host state decoupling allows managing different Toast visual element compositions.
    val hostState = SnackbarHostState()

    AppToast(
        snackbarHostState = hostState,
        toastType = type,
        onClick = {}
    )

    LaunchedEffect(key1 = id) {
        hostState.showSnackbar(
            message = title,
            actionLabel = description
        )
    }
}

@Composable
fun AppToast(
    snackbarHostState: SnackbarHostState,
    toastType: ToastType,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .zIndex(10f)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        SnackbarHost(
            modifier = Modifier.noRippleClickable {
                snackbarHostState.currentSnackbarData?.dismiss()
                onClick()
            },
            hostState = snackbarHostState,
            snackbar = { data -> ToastElement(data = data, type = toastType) }
        )
    }
}

@Composable
fun ToastElement(
    data: SnackbarData,
    type: ToastType
) {
    when (type) {
        ToastType.Error -> {
            ToastContent(data = data, resIcon = R.drawable.recco_ic_error)
        }

        else -> {}
    }
}

@Composable
fun ToastContent(
    data: SnackbarData,
    backgroundColor: Color = Color.White,
    titleStyle: TextStyle = AppTheme.typography.h4,
    subtitleStyle: TextStyle = AppTheme.typography.body2,
    @DrawableRes resIcon: Int = R.drawable.recco_ic_error
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppSpacing.dp_16)
            .padding(horizontal = AppSpacing.dp_16),
        shape = RoundedCornerShape(AppSpacing.dp_8),
        backgroundColor = backgroundColor,
        elevation = elevation.card
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppSpacing.dp_16)
                .padding(horizontal = AppSpacing.dp_16),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = resIcon),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(AppSpacing.dp_16))

            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = data.message,
                    style = titleStyle
                )

                Spacer(modifier = Modifier.height(AppSpacing.dp_8))

                Text(
                    text = data.actionLabel ?: stringResource(R.string.recco_common_error_desc),
                    style = subtitleStyle
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        ToastContent(data = object : SnackbarData {
            override val actionLabel = "Something went wrong."
            override val duration = SnackbarDuration.Short
            override val message = "Sorry !"

            override fun dismiss() {}
            override fun performAction() {}
        })
    }
}
