plugins {
    id("shadowflight.android.library")
    id("shadowflight.android.hilt")
}

android {
    namespace = "com.shadowflight.core.repository"
}

dependencies {
    implementation(project(":core:base"))
    implementation(project(":core:persistence"))
    implementation(project(":core:model"))
    implementation(project(":core:openapi"))
    implementation(project(":core:network"))
    implementation(project(":core:logger"))
}