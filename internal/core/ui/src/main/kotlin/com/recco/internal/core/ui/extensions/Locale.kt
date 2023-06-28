package com.recco.internal.core.ui.extensions

import com.recco.internal.core.model.questionnaire.UnitSystem
import java.util.Locale

fun Locale.toUnitSystem() =
    when (country.uppercase(Locale.ROOT)) {
        "US", "GB", "MM", "LR" -> UnitSystem.IMPERIAL
        else -> UnitSystem.METRIC
    }
