plugins {
    id("recco.android.library")
    id("recco.android.hilt")
    id("recco.android.spotless")
}

android {
    namespace = "com.recco.internal.core.network"
}

dependencies {
    implementation(project(":api:model"))
    implementation(project(":internal:core:base"))
    implementation(project(":internal:core:model"))
    implementation(project(":internal:core:persistence"))
    implementation(project(":internal:core:openapi"))
    implementation(project(":internal:core:logger"))

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    api(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)

    implementation(libs.moshi)
}
