plugins {
    id("recco.android.feature")
    id("recco.android.spotless")
}

android {
    namespace = "com.recco.internal.feature.article"

}

dependencies {
    "implementation"(project(":internal:core:media"))
    implementation(libs.exoplayer.ui)
}
