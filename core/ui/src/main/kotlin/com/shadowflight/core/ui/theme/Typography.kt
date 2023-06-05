package com.shadowflight.core.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.shadowflight.core.ui.R

private val poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semi_bold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

private val h1 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 28.sp,
    color = primary
)

private val h2 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.Medium,
    fontSize = 24.sp,
    color = primary
)

private val h3 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.Bold,
    fontSize = 17.sp,
    color = primary
)

private val h4 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 17.sp,
    color = primary
)

private val body1 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.Normal,
    fontSize = 17.sp,
    color = primary
)

private val body2 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.Medium,
    fontSize = 15.sp,
    color = primary
)

private val body3 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 12.sp,
    color = primary
)

private val cta = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    color = primary
)

private val labelSmall = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 13.sp,
    color = primary
)

private val contentTitle = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    color = primary
)

private val numberBig = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 22.sp,
    color = primary
)

internal val typography = Typography(
    defaultFontFamily = poppins,
    h1 = h1,
    h2 = h2,
    h3 = h3,
    h4 = h4,
    h5 = h4,
    h6 = h4,
    subtitle1 = labelSmall,
    subtitle2 = contentTitle,
    body1 = body1,
    body2 = body2,
    button = cta,
    caption = labelSmall,
    overline = contentTitle
)

@Immutable
data class ExtendedTypography(
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val h4: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val body3: TextStyle,
    val cta: TextStyle,
    val labelSmall: TextStyle,
    val contentTitle: TextStyle,
    val numberBig: TextStyle
)

val extendedTypography = ExtendedTypography(
    h1 = h1,
    h2 = h2,
    h3 = h3,
    h4 = h4,
    body1 = body1,
    body2 = body2,
    body3 = body3,
    cta = cta,
    labelSmall = labelSmall,
    contentTitle = contentTitle,
    numberBig = numberBig
)

internal val LocalExtendedTypography = staticCompositionLocalOf {
    extendedTypography
}

