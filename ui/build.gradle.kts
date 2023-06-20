plugins {
    id("shadowflight.android.feature")
    id("shadowflight.android.hilt")

}

android {
    namespace = "com.shadowflight.ui"
}

dependencies {
    api(project(":core:model"))
    implementation(project(":core:logger"))
    implementation(project(":core:repository"))
    implementation(project(":core:ui"))
    implementation(project(":feature:feed"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:article"))
    implementation(project(":feature:questionnaire"))
}