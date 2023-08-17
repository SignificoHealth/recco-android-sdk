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
import com.recco.api.model.ReccoFont
import com.recco.internal.core.ui.R

internal fun ReccoFont.asFontFamily(): FontFamily {
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )
    val fontName = GoogleFont(fontName)
    return FontFamily(
        Font(googleFont = fontName, fontProvider = provider, FontWeight.Normal),
        Font(googleFont = fontName, fontProvider = provider, FontWeight.Medium),
        Font(googleFont = fontName, fontProvider = provider, FontWeight.SemiBold),
        Font(googleFont = fontName, fontProvider = provider, FontWeight.Bold)
    )
}

private fun h1(textColor: Color, fontFamily: FontFamily) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 28.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun h2(textColor: Color, fontFamily: FontFamily) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 24.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun h3(textColor: Color, fontFamily: FontFamily) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 17.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun h4(textColor: Color, fontFamily: FontFamily) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 17.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun body1(textColor: Color, fontFamily: FontFamily) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 17.sp,
    color = textColor
)

private fun body1Bold(textColor: Color, fontFamily: FontFamily) =
    body1(textColor, fontFamily).copy(fontWeight = FontWeight.Bold)

private fun body2(textColor: Color, fontFamily: FontFamily) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 15.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun body2Bold(textColor: Color, fontFamily: FontFamily) =
    body2(textColor, fontFamily).copy(fontWeight = FontWeight.Bold)

private fun body3(textColor: Color, fontFamily: FontFamily) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 12.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun cta(textColor: Color, fontFamily: FontFamily) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun labelSmall(textColor: Color, fontFamily: FontFamily) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 13.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun contentTitle(textColor: Color, fontFamily: FontFamily) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = textColor
)

private fun numberBig(textColor: Color, fontFamily: FontFamily) = TextStyle(
    fontFamily = fontFamily,
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

internal fun extendedTypography(textColor: Color, fontFamily: FontFamily) = ExtendedTypography(
    h1 = h1(textColor, fontFamily),
    h2 = h2(textColor, fontFamily),
    h3 = h3(textColor, fontFamily),
    h4 = h4(textColor, fontFamily),
    body1 = body1(textColor, fontFamily),
    body1Bold = body1Bold(textColor, fontFamily),
    body2 = body2(textColor, fontFamily),
    body2Bold = body2Bold(textColor, fontFamily),
    body3 = body3(textColor, fontFamily),
    cta = cta(textColor, fontFamily),
    labelSmall = labelSmall(textColor, fontFamily),
    contentTitle = contentTitle(textColor, fontFamily),
    numberBig = numberBig(textColor, fontFamily)
)

internal fun ExtendedTypography.asTypography() = Typography(
    defaultFontFamily = ReccoFont.POPPINS.asFontFamily(),
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
