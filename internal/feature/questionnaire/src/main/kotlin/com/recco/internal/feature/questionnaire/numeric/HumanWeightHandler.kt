package com.recco.internal.feature.questionnaire.numeric

import com.recco.internal.core.model.questionnaire.UnitSystem
import com.recco.internal.core.model.questionnaire.gramsToKilograms
import com.recco.internal.core.model.questionnaire.kilogramsToPounds
import com.recco.internal.core.model.questionnaire.kilogramsToGrams
import com.recco.internal.core.model.questionnaire.poundsToGrams

internal class HumanWeightHandler(
    private val unitSystem: UnitSystem
) {
    val bigUnitLabel = when (unitSystem) {
        UnitSystem.IMPERIAL -> "lb"
        UnitSystem.METRIC -> "kg"
    }

    val bigUnitDecimals = when (unitSystem) {
        UnitSystem.IMPERIAL -> true
        UnitSystem.METRIC -> true
    }

    fun getBigUnitValue(kgs: Int): Double = when (unitSystem) {
        UnitSystem.IMPERIAL -> kgs.kilogramsToPounds()
        UnitSystem.METRIC -> kgs.toDouble()
    }

    fun getKilograms(bigValue: Double?) = getGrams(bigValue) / 1000

    private fun getGrams(bigValue: Double?): Double = when (unitSystem) {
        UnitSystem.IMPERIAL -> bigValue?.poundsToGrams() ?: 0.0
        UnitSystem.METRIC -> bigValue?.kilogramsToGrams() ?: 0.0
    }
}
