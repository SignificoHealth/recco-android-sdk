plugins {
    id("recco.android.library")
    id("recco.android.hilt")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.recco.core.model"
}

dependencies {
    api(libs.kotlinx.serialization.json)
}
