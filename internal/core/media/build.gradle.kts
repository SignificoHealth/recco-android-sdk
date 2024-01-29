plugins {
    id("recco.android.library")
    id("recco.android.hilt")
    id("recco.android.spotless")
    id("recco.android.library.compose")
}

android {
    namespace = "com.recco.internal.core.media"
}

dependencies {
    implementation(project(":api:model"))
    implementation(project(":internal:core:model"))
    implementation(project(":internal:core:ui"))
    implementation(libs.exoplayer.media.session)
    implementation(libs.exoplayer.ui)
    implementation(libs.exoplayer.core)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    api(libs.androidx.activity.compose)
    api(libs.androidx.compose.ui.util)
    api(libs.coil.compose)

}
