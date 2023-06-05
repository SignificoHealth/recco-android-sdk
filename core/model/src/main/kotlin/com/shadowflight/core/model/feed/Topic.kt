package com.shadowflight.core.model.feed

enum class Topic(val id: Int) {
    PHYSICAL_ACTIVITY(1),
    NUTRITION(2),
    PHYSICAL_WELLBEING(3),
    SLEEP(4);

    companion object {
        fun findById(id: Int): Topic = Topic.values().first { it.id == id }
    }
}