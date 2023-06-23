plugins {
    id("recco.android.feature")
}

android {
    namespace = "com.recco.internal.feature.onboarding"
}

dependencies {
    implementation(project(":internal:core:ui"))
}