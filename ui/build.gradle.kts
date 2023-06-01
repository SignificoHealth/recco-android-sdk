plugins {
    id("shadowflight.android.feature")
    id("shadowflight.android.hilt")

}

android {
    namespace = "com.shadowflight.ui"
}

dependencies {
    api(project(":core:model"))
    implementation(project(":core:persistence"))
    implementation(project(":core:ui"))
    implementation(project(":feature:feed"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:article"))
}