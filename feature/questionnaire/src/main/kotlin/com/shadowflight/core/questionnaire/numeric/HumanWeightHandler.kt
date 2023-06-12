package com.shadowflight.core.questionnaire.numeric

import com.shadowflight.core.model.questionnaire.UnitSystem
import com.shadowflight.core.model.questionnaire.gramsToKilograms
import com.shadowflight.core.model.questionnaire.gramsToPounds
import com.shadowflight.core.model.questionnaire.gramsToStones
import com.shadowflight.core.model.questionnaire.kilogramsToGrams
import com.shadowflight.core.model.questionnaire.poundsToGrams
import com.shadowflight.core.model.questionnaire.stonesToGrams

class HumanWeightHandler(
    private val unitSystem: UnitSystem
) {
    val bigUnitLabel = when (unitSystem) {
        UnitSystem.IMPERIAL_US -> "lb"
        UnitSystem.IMPERIAL_GB -> "st"
        UnitSystem.METRIC -> "kg"
    }

    val smallUnitLabel = when (unitSystem) {
        UnitSystem.IMPERIAL_US -> null
        UnitSystem.IMPERIAL_GB -> "lb"
        UnitSystem.METRIC -> null
    }

    val bigUnitDecimals = when (unitSystem) {
        UnitSystem.IMPERIAL_US -> true
        UnitSystem.IMPERIAL_GB -> false
        UnitSystem.METRIC -> true
    }

    val smallUnitChars = when (unitSystem) {
        UnitSystem.IMPERIAL_US -> 0
        UnitSystem.IMPERIAL_GB -> 2
        UnitSystem.METRIC -> 0
    }

    fun getBigUnitValue(grams: Int): Double = when (unitSystem) {
        UnitSystem.IMPERIAL_US -> grams.gramsToPounds()
        UnitSystem.IMPERIAL_GB -> grams.gramsToStones()
        UnitSystem.METRIC -> grams.gramsToKilograms()
    }

    fun getKilograms(bigValue: Double?, smallValue: Double?) = getGrams(bigValue, smallValue) / 1000

    private fun getGrams(bigValue: Double?, smallValue: Double?): Double = when (unitSystem) {
        UnitSystem.IMPERIAL_US -> bigValue?.poundsToGrams() ?: 0.0
        UnitSystem.IMPERIAL_GB -> (bigValue?.stonesToGrams() ?: 0.0) + (smallValue?.poundsToGrams()
            ?: 0.0)

        UnitSystem.METRIC -> bigValue?.kilogramsToGrams() ?: 0.0
    }
}
