plugins {
    id("shadowflight.android.feature")
}

android {
    namespace = "com.shadowflight.feature.feed"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:logger"))
    implementation(project(":core:model"))
    implementation(project(":core:repository"))
}