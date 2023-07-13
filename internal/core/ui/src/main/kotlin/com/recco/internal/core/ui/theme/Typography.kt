@file:Suppress(
    "DEPRECATION",
    "unused"
)

package com.recco.internal.core.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.recco.api.model.ReccoPalette
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

private val textColor = ReccoPalette.Default.lightColors.primary

private val h1 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 28.sp,
    color = textColor,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)

private val h2 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.Medium,
    fontSize = 24.sp,
    color = textColor,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)

private val h3 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.Bold,
    fontSize = 17.sp,
    color = textColor,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)

private val h4 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 17.sp,
    color = textColor,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)

private val body1 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.Normal,
    fontSize = 17.sp,
    color = textColor,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)

private val body1Bold = body1.copy(fontWeight = FontWeight.Bold)

private val body2 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.Medium,
    fontSize = 15.sp,
    color = textColor,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)

private val body2Bold = body2.copy(fontWeight = FontWeight.Bold)


private val body3 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 12.sp,
    color = textColor,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)

private val cta = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    color = textColor,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)

private val labelSmall = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 13.sp,
    color = textColor,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)

private val contentTitle = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    color = textColor,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)

private val numberBig = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 22.sp,
    color = textColor,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
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
    val body1Bold: TextStyle,
    val body2: TextStyle,
    val body2Bold: TextStyle,
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
    body1Bold = body1Bold,
    body2 = body2,
    body2Bold = body2Bold,
    body3 = body3,
    cta = cta,
    labelSmall = labelSmall,
    contentTitle = contentTitle,
    numberBig = numberBig
)

internal val LocalExtendedTypography = staticCompositionLocalOf {
    extendedTypography
}

