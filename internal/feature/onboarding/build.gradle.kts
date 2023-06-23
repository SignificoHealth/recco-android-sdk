plugins {
    id("recco.android.feature")
}

android {
    namespace = "com.recco.internal.feature.onboarding"
}

dependencies {
    implementation(project(":api:model"))
    implementation(project(":internal:core:model"))
    implementation(project(":internal:core:ui"))
}