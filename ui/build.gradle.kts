plugins {
    id("recco.android.feature")
    id("recco.android.hilt")

}

android {
    namespace = "com.recco.ui"
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
    implementation(project(":feature:bookmark"))
}