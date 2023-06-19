plugins {
    id("shadowflight.android.feature")
}

android {
    namespace = "com.shadowflight.core.onboarding"
}

dependencies {
    implementation(project(":core:ui"))
}