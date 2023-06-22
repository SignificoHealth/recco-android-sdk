plugins {
    id("recco.android.feature")
}

android {
    namespace = "com.recco.core.onboarding"
}

dependencies {
    implementation(project(":core:ui"))
}