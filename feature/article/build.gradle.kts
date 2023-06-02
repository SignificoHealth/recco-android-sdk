plugins {
    id("shadowflight.android.feature")
}

android {
    namespace = "com.shadowflight.article"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:logger"))
    implementation(project(":core:model"))
    implementation(project(":core:repository"))
}