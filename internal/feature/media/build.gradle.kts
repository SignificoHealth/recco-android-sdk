plugins {
    id("recco.android.feature")
    id("recco.android.spotless")
}

android {
    namespace = "com.recco.internal.feature.media"
}

dependencies {
    implementation(project(":internal:core:media"))
    implementation(project(":internal:feature:rating"))
    implementation(libs.exoplayer.media.session)
    implementation(libs.exoplayer.ui)
}
