plugins {
    id("recco.android.library")
    id("recco.android.hilt")
}

android {
    namespace = "com.recco.core.network"
}

dependencies {
    implementation(project(":core:base"))
    implementation(project(":core:model"))
    implementation(project(":core:persistence"))
    implementation(project(":core:openapi"))
    implementation(project(":core:logger"))

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    api(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)

    implementation(libs.moshi)
}