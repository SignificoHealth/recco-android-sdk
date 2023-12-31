plugins {
    id("recco.android.library")
    id("recco.android.hilt")
    id("recco.android.spotless")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.recco.internal.core.model"
}

dependencies {
    implementation(project(":api:model"))
    api(libs.kotlinx.serialization.json)
}
