package com.recco.showcase.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recco.api.model.ReccoFont
import com.recco.showcase.R
import com.recco.showcase.ui.theme.SoftYellow
import com.recco.showcase.ui.theme.Typography

@Composable
fun FontSelection(
    selectionClickStyle: (ReccoFont) -> Unit,
    initiallyExpanded: Boolean = false,
    selectedFont: ReccoFont? = null
) {
    val expanded = remember { mutableStateOf(initiallyExpanded) }

    CustomDropdown(
        expandedState = expanded,
        iconRes = R.drawable.ic_font,
        topIconMargin = 40.dp
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .background(color = Color.White)
        ) {
            ReccoFont.values().forEach { font ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .let {
                            if (selectedFont == font) {
                                it.border(2.dp, SoftYellow)
                            } else {
                                it
                            }
                        }
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .clickable {
                            selectionClickStyle(font)
                            expanded.value = false
                        }
                ) {
                    Text(
                        text = font.fontName,
                        style = Typography.labelSmall.copy(
                            fontFamily = font.asFontFamily()
                        )
                    )
                }
                Divider()
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    FontSelection(selectionClickStyle = {}, initiallyExpanded = true, selectedFont = null)
}

@Preview
@Composable
private fun PreviewSelectedFont() {
    FontSelection(selectionClickStyle = {}, initiallyExpanded = true, selectedFont = ReccoFont.ROBOTO)
}

private fun ReccoFont.asFontFamily(): FontFamily {
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = com.recco.internal.core.ui.R.array.com_google_android_gms_fonts_certs
    )
    val fontName = GoogleFont(fontName)
    return FontFamily(
        Font(googleFont = fontName, fontProvider = provider, FontWeight.Normal),
        Font(googleFont = fontName, fontProvider = provider, FontWeight.Medium),
        Font(googleFont = fontName, fontProvider = provider, FontWeight.SemiBold),
        Font(googleFont = fontName, fontProvider = provider, FontWeight.Bold)
    )
}
