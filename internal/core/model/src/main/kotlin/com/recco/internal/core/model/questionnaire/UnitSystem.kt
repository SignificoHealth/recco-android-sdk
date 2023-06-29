package com.recco.internal.core.model.questionnaire

import kotlin.math.floor

enum class UnitSystem {
    IMPERIAL,
    METRIC
}

fun Double.poundsToGrams(): Double = this * 453.59237
fun Int.kilogramsToPounds(): Double = this * 2.2
fun Double.kilogramsToGrams(): Double = this * 1000
fun Int.gramsToKilograms(): Double = this / 1000.0
fun Double.feetToMillimeters(): Int = (this * 12 * 25.4).toInt()
fun Int.centimetersToFeetWithNoInches(): Int = floor(this / 30.48).toInt()
fun Int.centimetersToRemainingInches(): Int =
    ((this / 30.48 - centimetersToFeetWithNoInches()) * 12).toInt()

fun Double.inchesToMillimeters(): Int = (this * 25.4).toInt()
fun Double.metersToMillimeters(): Int = (this * 1000).toInt()
fun Int.centimetersToMeters(): Int = floor(this / 100.0).toInt()
fun Double.centimetersToMillimeters(): Int = (this * 10).toInt()