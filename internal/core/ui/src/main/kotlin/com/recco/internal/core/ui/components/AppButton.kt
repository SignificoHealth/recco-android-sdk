package com.recco.internal.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme

@Composable
internal fun AppButton(
    modifier: Modifier = Modifier,
    isPrimary: Boolean = true,
    text: String? = null,
    @DrawableRes iconStartRes: Int? = null,
    @DrawableRes iconEndRes: Int? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val isEnabled = enabled && !isLoading
    val backgroundColor = getBackgroundColor(isPrimary, isEnabled)
    val contentColor = getContentColor(isPrimary, isEnabled)
    val borderStroke = getBorderStroke(isPrimary, isEnabled)
    val cornerShape = RoundedCornerShape(4.dp)

    Button(
        modifier = modifier
            .defaultMinSize(minHeight = 40.dp)
            .height(48.dp)
            .clip(cornerShape),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            disabledBackgroundColor = backgroundColor
        ),
        shape = cornerShape,
        border = borderStroke,
        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
        onClick = onClick,
        enabled = isEnabled,
        content = {
            iconStartRes?.let {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(iconStartRes),
                    contentDescription = null,
                    tint = contentColor
                )
            }

            text?.let {
                Text(
                    modifier = Modifier.padding(horizontal = AppSpacing.dp_8),
                    text = text,
                    style = AppTheme.typography.cta,
                    color = contentColor
                )
            }

            when {
                isLoading -> {
                    AppProgressLoading(
                        size = 20.dp,
                        strokeWidth = 1.5.dp,
                        color = contentColor
                    )
                }

                else -> iconEndRes?.let {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(iconEndRes),
                        contentDescription = null,
                        tint = contentColor
                    )
                }
            }
        }
    )
}

@Composable
private fun getBackgroundColor(
    isPrimary: Boolean,
    enabled: Boolean
): Color {
    val color = if (isPrimary) {
        AppTheme.colors.primary
    } else {
        AppTheme.colors.background
    }

    return if (enabled) color else color.copy(alpha = getAlpha())
}

@Composable
private fun getContentColor(
    isPrimary: Boolean,
    enabled: Boolean
): Color {
    val color = if (isPrimary) {
        AppTheme.colors.onPrimary
    } else {
        AppTheme.colors.primary
    }

    return if (enabled) color else color.copy(alpha = getContentAlpha(isPrimary))
}

@Composable
private fun getBorderColor(isPrimary: Boolean, enabled: Boolean): Color {
    val color = if (isPrimary) {
        AppTheme.colors.primary
    } else {
        AppTheme.colors.primary10
    }

    return if (enabled) color else color.copy(alpha = getAlpha())
}

@Composable
private fun getBorderStroke(
    isPrimary: Boolean,
    enabled: Boolean
): BorderStroke? {
    val color = getBorderColor(isPrimary, enabled)
    return if (isPrimary) null else BorderStroke(1.5.dp, color)
}

@Composable
private fun getAlpha(): Float {
    return 0.2f
}

@Composable
private fun getContentAlpha(isPrimary: Boolean): Float {
    return if (isPrimary) 1f else 0.2f
}
