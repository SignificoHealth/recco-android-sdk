package com.recco.showcase.customize

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import com.recco.api.model.ReccoPalette
import com.recco.showcase.R
import com.recco.showcase.main.asComposeColor
import com.recco.showcase.ui.theme.BackgroundColor
import com.recco.showcase.ui.theme.SoftYellow
import com.recco.showcase.ui.theme.WarmBrown

@Composable
fun ColorPickerScreen(
    showState: MutableState<Boolean>,
    color: String,
    updateColor: (String) -> Unit
) {
    val controller = rememberColorPickerController()

    if (showState.value) {
        var updatedColor = color

        BottomSheetDialog(
            onDismissRequest = {
                updateColor("#$updatedColor")
                showState.value = false
            },
            properties = BottomSheetDialogProperties()
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(BackgroundColor)
                        .padding(all = 24.dp)
                ) {
                    Surface(
                        modifier = Modifier.align(Alignment.End),
                        shape = CircleShape,
                        shadowElevation = 3.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .background(WarmBrown)
                                .size(40.dp)
                                .clickable {
                                    updateColor("#$updatedColor")
                                    showState.value = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier.size(25.dp),
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(SoftYellow)
                            )
                        }
                    }

                    HsvColorPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(450.dp),
                        controller = controller,
                        onColorChanged = { colorEnvelope ->
                            updatedColor = colorEnvelope.hexCode
                        },
                        initialColor = color.asComposeColor()
                    )
                    AlphaSlider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(35.dp),
                        controller = controller,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val showState = remember { mutableStateOf(true) }
    ColorPickerScreen(showState = showState, color = ReccoPalette.Fresh.lightColors.accent, updateColor = {})
}
