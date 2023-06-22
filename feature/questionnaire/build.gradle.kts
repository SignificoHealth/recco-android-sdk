plugins {
    id("recco.android.feature")
}

android {
    namespace = "com.recco.core.questionnaire"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:logger"))
    implementation(project(":core:model"))
    implementation(project(":core:repository"))
}