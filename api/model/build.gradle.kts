plugins {
    id("recco.android.library.compose")
    id("recco.android.hilt")
    id("recco.android.spotless")
}

android {
    namespace = "com.recco.api.model"
}

dependencies {
    implementation(libs.androidx.compose.material)
}
