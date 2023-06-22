plugins {
    id("recco.android.library")
    id("recco.android.hilt")
}

android {
    namespace = "com.recco.core.repository"
}

dependencies {
    implementation(project(":core:base"))
    implementation(project(":core:persistence"))
    implementation(project(":core:model"))
    implementation(project(":core:openapi"))
    implementation(project(":core:network"))
    implementation(project(":core:logger"))
}