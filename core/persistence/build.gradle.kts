plugins {
    id("recco.android.library")
    id("recco.android.hilt")
}

android {
    namespace = "com.recco.core.persistence"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:logger"))

    implementation(libs.security)
}