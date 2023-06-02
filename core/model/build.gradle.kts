plugins {
    id("shadowflight.android.library")
    id("shadowflight.android.hilt")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.shadowflight.model"
}

dependencies {
    api(libs.kotlinx.serialization.json)
}
