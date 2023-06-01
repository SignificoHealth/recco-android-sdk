plugins {
    id("shadowflight.android.library")
    id("shadowflight.android.hilt")
}

android {
    namespace = "com.shadowflight.persistence"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:logger"))

    implementation(libs.security)
}