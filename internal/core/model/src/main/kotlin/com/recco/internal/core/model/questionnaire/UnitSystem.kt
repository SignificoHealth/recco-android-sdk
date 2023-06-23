package com.recco.internal.core.model.questionnaire

enum class UnitSystem {
    IMPERIAL_US,
    IMPERIAL_GB,
    METRIC
}

fun Double.stonesToGrams(): Double = this * 14 * 453.59237
fun Int.gramsToStones(): Double = this / 14 / 453.59237
fun Double.poundsToGrams(): Double = this * 453.59237
fun Int.gramsToPounds(): Double = this / 453.59237
fun Double.kilogramsToGrams(): Double = this * 1000
fun Int.gramsToKilograms(): Double = this / 1000.0
fun Double.feetToMillimeters(): Int = (this * 12 * 25.4).toInt()
fun Int.millimetersToFeet(): Double = this / 12 / 25.4
fun Double.inchesToMillimeters(): Int = (this * 25.4).toInt()
fun Double.metersToMillimeters(): Int = (this * 1000).toInt()
fun Int.millimetersToMeters(): Double = this / 1000.0
fun Double.centimetersToMillimeters(): Int = (this * 10).toInt()