package com.recco.internal.core.ui.extensions

import com.recco.internal.core.model.questionnaire.UnitSystem
import java.util.Locale

fun Locale.toUnitSystem() =
    when (country.uppercase(Locale.ROOT)) {
        "US" -> UnitSystem.IMPERIAL_US
        "GB", "MM", "LR" -> UnitSystem.IMPERIAL_GB // UK, Myanmar, Liberia
        else -> UnitSystem.METRIC
    }
