plugins {
    id("recco.android.library")
    id("recco.android.hilt")
    id("recco.android.spotless")
}

android {
    namespace = "com.recco.internal.core.media"
}

dependencies {
    implementation(project(":api:model"))
    implementation(project(":internal:core:model"))
    implementation(libs.exoplayer.media.session)
    implementation(libs.exoplayer.ui)
    implementation(libs.exoplayer.core)
}
