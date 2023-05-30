plugins {
    id("shadowflight.android.feature")
}

android {
    namespace = "com.shadowflight.feed"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:repository"))
}