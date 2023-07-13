plugins {
    id("recco.android.library.compose")
    id("recco.android.hilt")
}

android {
    namespace = "com.recco.api.model"
}

dependencies {
    implementation(libs.androidx.compose.material)
}