plugins {
    id("recco.android.feature")
    id("recco.android.hilt")
    id("recco.android.spotless")

}

android {
    namespace = "com.recco.api.ui"
}

dependencies {
    api(project(":api:model"))
    implementation(project(":internal:feature:feed"))
    implementation(project(":internal:feature:onboarding"))
    implementation(project(":internal:feature:article"))
    implementation(project(":internal:feature:questionnaire"))
    implementation(project(":internal:feature:bookmark"))
    implementation(libs.androidx.lifecycle.process)
}
