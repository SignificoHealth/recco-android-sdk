package com.shadowflight.core.ui.extensions

import com.shadowflight.core.model.questionnaire.UnitSystem
import java.util.Locale

fun Locale.toUnitSystem() =
    when (country.uppercase(Locale.ROOT)) {
        "US" -> UnitSystem.IMPERIAL_US
        "GB", "MM", "LR" -> UnitSystem.IMPERIAL_GB // UK, Myanmar, Liberia
        else -> UnitSystem.METRIC
    }
