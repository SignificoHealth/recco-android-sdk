plugins {
    id("recco.android.feature")
    id("recco.android.hilt")

}

android {
    namespace = "com.recco.api.ui"
}

dependencies {
    api(project(":api:model"))
    implementation(project(":internal:core:model"))
    implementation(project(":internal:core:logger"))
    implementation(project(":internal:core:repository"))
    implementation(project(":internal:core:ui"))
    implementation(project(":internal:feature:feed"))
    implementation(project(":internal:feature:onboarding"))
    implementation(project(":internal:feature:article"))
    implementation(project(":internal:feature:questionnaire"))
    implementation(project(":internal:feature:bookmark"))
}