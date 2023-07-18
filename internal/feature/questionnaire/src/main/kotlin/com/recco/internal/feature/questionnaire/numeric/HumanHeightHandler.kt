package com.recco.internal.feature.questionnaire.numeric

import com.recco.internal.core.model.questionnaire.UnitSystem
import com.recco.internal.core.model.questionnaire.centimetersToFeetWithNoInches
import com.recco.internal.core.model.questionnaire.centimetersToMeters
import com.recco.internal.core.model.questionnaire.centimetersToMillimeters
import com.recco.internal.core.model.questionnaire.centimetersToRemainingInches
import com.recco.internal.core.model.questionnaire.feetToMillimeters
import com.recco.internal.core.model.questionnaire.inchesToMillimeters
import com.recco.internal.core.model.questionnaire.metersToMillimeters

internal class HumanHeightHandler(
    private val unitSystem: UnitSystem
) {
    val bigUnitLabel = when (unitSystem) {
        UnitSystem.IMPERIAL -> "ft"
        UnitSystem.METRIC -> "cm"
    }

    val smallUnitLabel = when (unitSystem) {
        UnitSystem.IMPERIAL -> "in"
        UnitSystem.METRIC -> "cm"
    }

    val smallUnitChars = 2

    fun getBigUnitValue(centimeters: Int): Int = when (unitSystem) {
        UnitSystem.IMPERIAL -> centimeters.centimetersToFeetWithNoInches()
        UnitSystem.METRIC -> centimeters.centimetersToMeters()
    }

    fun getSmallUnitValue(centimeters: Int): Int = when (unitSystem) {
        UnitSystem.IMPERIAL -> centimeters.centimetersToRemainingInches()
        UnitSystem.METRIC -> throw IllegalStateException("Does not apply")
    }

    fun getCentimeters(bigValue: Double?, smallValue: Double?): Int =
        getMillimeters(bigValue, smallValue) / 10

    private fun getMillimeters(bigValue: Double?, smallValue: Double?): Int = when (unitSystem) {
        UnitSystem.IMPERIAL -> (
            bigValue?.feetToMillimeters()
                ?: 0
            ) + (smallValue?.inchesToMillimeters() ?: 0)

        UnitSystem.METRIC -> (
            bigValue?.metersToMillimeters()
                ?: 0
            ) + (smallValue?.centimetersToMillimeters() ?: 0)
    }
}
