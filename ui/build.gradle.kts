plugins {
    id("shadowflight.android.feature")
}

android {
    namespace = "com.shadowflight.ui"
}

dependencies {
    implementation(project(":feature:feed"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:article"))
}