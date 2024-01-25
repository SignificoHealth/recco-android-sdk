plugins {
    id("recco.android.feature")
    id("recco.android.spotless")
}

android {
    namespace = "com.recco.internal.feature.media"

}

dependencies {
    implementation(project(":internal:core:media"))
    implementation(libs.exoplayer.media.session)
    implementation(libs.exoplayer.ui)
    implementation(libs.exoplayer.core)
}
