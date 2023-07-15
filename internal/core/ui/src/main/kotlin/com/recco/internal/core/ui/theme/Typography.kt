@file:Suppress(
    "DEPRECATION",
    "unused"
)

package com.recco.internal.core.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.recco.internal.core.ui.R

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)
private val fontName = GoogleFont("Poppins")
private val poppins = FontFamily(
    Font(googleFont = fontName, fontProvider = provider, FontWeight.Normal),
    Font(googleFont = fontName, fontProvider = provider, FontWeight.Medium),
    Font(googleFont = fontName, fontProvider = provider, FontWeight.SemiBold),
    Font(googleFont = fontName, fontProvider = provider, FontWeight.Bold)
)

private fun h1(textColor: Color) = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 28.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun h2(textColor: Color) = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.Medium,
    fontSize = 24.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun h3(textColor: Color) = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.Bold,
    fontSize = 17.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun h4(textColor: Color) = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 17.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun body1(textColor: Color) = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.Normal,
    fontSize = 17.sp,
    color = textColor
)

private fun body1Bold(textColor: Color) = body1(textColor).copy(fontWeight = FontWeight.Bold)

private fun body2(textColor: Color) = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.Medium,
    fontSize = 15.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun body2Bold(textColor: Color) = body2(textColor).copy(fontWeight = FontWeight.Bold)

private fun body3(textColor: Color) = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 12.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun cta(textColor: Color) = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun labelSmall(textColor: Color) = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 13.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun contentTitle(textColor: Color) = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun numberBig(textColor: Color) = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 22.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

@Immutable
data class ExtendedTypography(
    val h1: TextStyle = TextStyle(),
    val h2: TextStyle = TextStyle(),
    val h3: TextStyle = TextStyle(),
    val h4: TextStyle = TextStyle(),
    val body1: TextStyle = TextStyle(),
    val body1Bold: TextStyle = TextStyle(),
    val body2: TextStyle = TextStyle(),
    val body2Bold: TextStyle = TextStyle(),
    val body3: TextStyle = TextStyle(),
    val cta: TextStyle = TextStyle(),
    val labelSmall: TextStyle = TextStyle(),
    val contentTitle: TextStyle = TextStyle(),
    val numberBig: TextStyle = TextStyle()
)

internal fun extendedTypography(textColor: Color) = ExtendedTypography(
    h1 = h1(textColor),
    h2 = h2(textColor),
    h3 = h3(textColor),
    h4 = h4(textColor),
    body1 = body1(textColor),
    body1Bold = body1Bold(textColor),
    body2 = body2(textColor),
    body2Bold = body2Bold(textColor),
    body3 = body3(textColor),
    cta = cta(textColor),
    labelSmall = labelSmall(textColor),
    contentTitle = contentTitle(textColor),
    numberBig = numberBig(textColor)
)

internal fun ExtendedTypography.asTypography() = Typography(
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

internal val LocalExtendedTypography = staticCompositionLocalOf {
    ExtendedTypography()
}
