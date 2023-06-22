package com.recco.core.questionnaire.numeric

import com.recco.core.model.questionnaire.UnitSystem
import com.recco.core.model.questionnaire.centimetersToMillimeters
import com.recco.core.model.questionnaire.feetToMillimeters
import com.recco.core.model.questionnaire.inchesToMillimeters
import com.recco.core.model.questionnaire.metersToMillimeters
import com.recco.core.model.questionnaire.millimetersToFeet
import com.recco.core.model.questionnaire.millimetersToMeters

class HumanHeightHandler(
    private val unitSystem: UnitSystem
) {
    val bigUnitLabel = when (unitSystem) {
        UnitSystem.IMPERIAL_US,
        UnitSystem.IMPERIAL_GB -> "ft"
        UnitSystem.METRIC -> "cm"
    }

    val smallUnitLabel = when (unitSystem) {
        UnitSystem.IMPERIAL_US,
        UnitSystem.IMPERIAL_GB -> "in"
        UnitSystem.METRIC -> "cm"
    }

    val smallUnitChars = 2

    fun getBigUnitValue(millimeters: Int): Double = when (unitSystem) {
        UnitSystem.IMPERIAL_US,
        UnitSystem.IMPERIAL_GB -> millimeters.millimetersToFeet()

        UnitSystem.METRIC -> millimeters.millimetersToMeters()
    }

    fun getCentimeters(bigValue: Double?, smallValue: Double?): Int =
        getMillimeters(bigValue, smallValue) / 10

    private fun getMillimeters(bigValue: Double?, smallValue: Double?): Int = when (unitSystem) {
        UnitSystem.IMPERIAL_US,
        UnitSystem.IMPERIAL_GB -> (bigValue?.feetToMillimeters()
            ?: 0) + (smallValue?.inchesToMillimeters() ?: 0)

        UnitSystem.METRIC -> (bigValue?.metersToMillimeters()
            ?: 0) + (smallValue?.centimetersToMillimeters() ?: 0)
    }
}
