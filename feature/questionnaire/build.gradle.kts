plugins {
    id("shadowflight.android.feature")
}

android {
    namespace = "com.shadowflight.questionnaire"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:logger"))
    implementation(project(":core:model"))
}