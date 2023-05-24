plugins {
    id("shadowflight.android.library")
}

android {
    namespace = "com.shadowflight.headless"
}

dependencies {
    implementation(project(":core:network"))
}