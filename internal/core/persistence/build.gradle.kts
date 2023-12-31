plugins {
    id("recco.android.library")
    id("recco.android.hilt")
    id("recco.android.spotless")
}

android {
    namespace = "com.recco.internal.core.persistence"
}

dependencies {
    implementation(project(":api:model"))
    implementation(project(":internal:core:model"))
    implementation(project(":internal:core:logger"))

    implementation(libs.security)
}
