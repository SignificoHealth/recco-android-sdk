plugins {
    id("recco.android.feature")
}

android {
    namespace = "com.recco.feature.article"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:logger"))
    implementation(project(":core:model"))
    implementation(project(":core:repository"))
}