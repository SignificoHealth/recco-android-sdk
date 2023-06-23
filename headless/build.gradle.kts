plugins {
    id("recco.android.library")
}

android {
    namespace = "com.recco.headless"
}

dependencies {
    implementation(project(":internal:core:network"))
}