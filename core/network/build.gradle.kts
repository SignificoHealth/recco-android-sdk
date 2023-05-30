plugins {
    id("shadowflight.android.library")
    id("shadowflight.android.hilt")
}

android {
    namespace = "com.shadowflight.network"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:openapi"))
    implementation(project(":core:logger"))

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    api(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)

    implementation(libs.moshi)
}