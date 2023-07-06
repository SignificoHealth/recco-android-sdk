plugins {
    id("recco.android.library")
}

android {
    namespace = "com.recco.internal.core.test"
}

dependencies {
    implementation(project(":internal:core:ui"))

    compileOnly(libs.junit.engine)

    implementation(libs.junit.api)
    implementation(libs.coroutines.test)
    implementation(libs.androidx.test.runner)
    implementation(libs.androidx.test.rules)
}