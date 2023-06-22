package com.recco.core.questionnaire.numeric

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.recco.core.model.questionnaire.NumericQuestionFormat
import com.recco.core.model.questionnaire.UnitSystem
import com.recco.core.ui.extensions.toUnitSystem
import com.recco.core.ui.theme.AppSpacing
import java.util.Locale

@Composable
fun NumericInput(
    onValueChange: (String) -> Unit,
    format: NumericQuestionFormat,
    maxValue: Int,
    initialValue: String,
) {
    when (format) {
        NumericQuestionFormat.INTEGER, NumericQuestionFormat.DECIMAL -> NumericTextField(
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onValueChange,
            supportDecimal = format == NumericQuestionFormat.DECIMAL,
            maxChars = maxValue.toString().length,
            initialValue = initialValue
        )

        NumericQuestionFormat.HUMAN_HEIGHT -> HumanHeightInput(
            onValueChange = onValueChange,
            maxValue = maxValue
        )

        NumericQuestionFormat.HUMAN_WEIGHT -> HumanWeightInput(
            onValueChange = onValueChange,
            maxValue = maxValue
        )
    }
}

@Composable
private fun HumanHeightInput(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    maxValue: Int,
    handler: HumanHeightHandler = HumanHeightHandler(Locale.getDefault().toUnitSystem())
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        if (handler.bigUnitLabel != handler.smallUnitLabel) {
            var bigUnitValue: String? by remember { mutableStateOf(null) }
            var smallUnitValue: String? by remember { mutableStateOf(null) }

            NumericTextField(
                modifier = Modifier.weight(.5f),
                onValueChange = { newValue ->
                    bigUnitValue = newValue
                    onValueChange(
                        handler.getCentimeters(
                            bigValue = bigUnitValue?.toDoubleOrNull(),
                            smallValue = smallUnitValue?.toDoubleOrNull()
                        ).toDouble().toString()
                    )
                },
                supportDecimal = false,
                maxChars = handler.getBigUnitValue(maxValue).toInt().toString().length,
                label = handler.bigUnitLabel
            )
            Spacer(modifier = Modifier.width(AppSpacing.dp_24))

            NumericTextField(
                modifier = Modifier.weight(.5f),
                onValueChange = { newValue ->
                    smallUnitValue = newValue
                    onValueChange(
                        handler.getCentimeters(
                            bigValue = bigUnitValue?.toDoubleOrNull(),
                            smallValue = smallUnitValue?.toDoubleOrNull()
                        ).toDouble().toString()
                    )
                },
                maxChars = handler.smallUnitChars,
                label = handler.smallUnitLabel
            )
        } else {
            var smallUnitValue: String? by remember { mutableStateOf(null) }
            NumericTextField(
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { newValue ->
                    smallUnitValue = newValue
                    onValueChange(
                        handler.getCentimeters(
                            bigValue = null,
                            smallValue = smallUnitValue?.toDoubleOrNull()
                        ).toDouble().toString()
                    )
                },
                maxChars = 3,
                label = handler.smallUnitLabel
            )
        }
    }
}

@Composable
private fun HumanWeightInput(
    onValueChange: (String) -> Unit,
    maxValue: Int,
    handler: HumanWeightHandler = HumanWeightHandler(Locale.getDefault().toUnitSystem())
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        if (handler.smallUnitLabel != null) {
            var bigUnitValue: String? by remember { mutableStateOf(null) }
            var smallUnitValue: String? by remember { mutableStateOf(null) }

            Row(Modifier.weight(.15f)) {}
            NumericTextField(
                modifier = Modifier.weight(.5f),
                supportDecimal = handler.bigUnitDecimals,
                label = handler.bigUnitLabel,
                hint = if (handler.bigUnitDecimals) "0.00" else "0",
                maxChars = if (handler.bigUnitDecimals) {
                    handler.getBigUnitValue(maxValue).toInt().toString().length + 3
                } else {
                    handler.getBigUnitValue(maxValue).toInt().toString().length
                },
                onValueChange = { newValue ->
                    bigUnitValue = newValue
                    onValueChange(
                        handler.getKilograms(
                            bigValue = bigUnitValue?.toDoubleOrNull(),
                            smallValue = smallUnitValue?.toDoubleOrNull()
                        ).toString()
                    )
                }
            )
            Spacer(modifier = Modifier.width(AppSpacing.dp_24))
            NumericTextField(
                modifier = Modifier.weight(.5f),
                hint = "0",
                label = handler.smallUnitLabel,
                maxChars = handler.smallUnitChars,
                onValueChange = { newValue ->
                    smallUnitValue = newValue
                    onValueChange(
                        handler.getKilograms(
                            bigValue = bigUnitValue?.toDoubleOrNull(),
                            smallValue = smallUnitValue?.toDoubleOrNull()
                        ).toString()
                    )
                }
            )
            Row(Modifier.weight(.15f)) {}
        } else {
            var bigUnitValue: String? by remember { mutableStateOf(null) }

            NumericTextField(
                modifier = Modifier.fillMaxWidth(),
                supportDecimal = handler.bigUnitDecimals,
                label = handler.bigUnitLabel,
                hint = if (handler.bigUnitDecimals) "0.00" else "0",
                maxChars = if (handler.bigUnitDecimals) {
                    handler.getBigUnitValue(maxValue).toInt().toString().length + 3
                } else {
                    handler.getBigUnitValue(maxValue).toInt().toString().length
                },
                onValueChange = { newValue ->
                    bigUnitValue = newValue
                    onValueChange(
                        handler.getKilograms(
                            bigValue = bigUnitValue?.toDoubleOrNull(),
                            smallValue = null
                        ).toString()
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewInteger() {
    NumericInput(
        onValueChange = {},
        format = NumericQuestionFormat.INTEGER,
        maxValue = 34,
        initialValue = ""
    )
}

@Preview
@Composable
private fun PreviewDecimal() {
    NumericInput(
        onValueChange = {},
        format = NumericQuestionFormat.DECIMAL,
        maxValue = 34,
        initialValue = ""
    )
}

@Preview
@Composable
private fun PreviewHeightImperial() {
    HumanHeightInput(
        onValueChange = {},
        maxValue = 34,
        handler = HumanHeightHandler(UnitSystem.IMPERIAL_US)
    )
}

@Preview
@Composable
private fun PreviewHeightMetric() {
    HumanHeightInput(
        onValueChange = {},
        maxValue = 34,
        handler = HumanHeightHandler(UnitSystem.METRIC)
    )
}

@Preview
@Composable
private fun PreviewWeightImperial() {
    HumanWeightInput(
        onValueChange = {},
        maxValue = 34,
        handler = HumanWeightHandler(UnitSystem.IMPERIAL_US)
    )
}

@Preview
@Composable
private fun PreviewWeightMetric() {
    HumanWeightInput(
        onValueChange = {},
        maxValue = 34,
        handler = HumanWeightHandler(UnitSystem.METRIC)
    )
}