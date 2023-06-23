plugins {
    id("recco.android.feature")
}

android {
    namespace = "com.recco.internal.feature.bookmark"
}

dependencies {
    implementation(project(":internal:core:ui"))
    implementation(project(":internal:core:logger"))
    implementation(project(":internal:core:model"))
    implementation(project(":internal:core:repository"))
}