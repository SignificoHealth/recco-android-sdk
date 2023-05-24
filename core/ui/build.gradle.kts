plugins {
    id("shadowflight.android.library")
}

android {
    namespace = "com.shadowflight.ui"
}

dependencies {
    api(libs.androidx.activity.compose)

    debugApi(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)

    api(libs.androidx.compose.material)
    api(libs.androidx.compose.material.icons.extended)

    api(libs.accompanist.flowlayout)
    api(libs.accompanist.insets.ui)
    api(libs.accompanist.pager.indicators)
    api(libs.accompanist.systemuicontroller)

    api(libs.coil.compose)

    api(libs.lottie)

    api(libs.material)
}
