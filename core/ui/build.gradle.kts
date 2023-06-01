plugins {
    id("shadowflight.android.library.compose")
}

android {
    // Changed namespace due to inconsistency building release variant
    namespace = "com.shadowflight.uicommons"
}

dependencies {
    implementation(project(":core:model"))

    api(libs.androidx.activity.compose)

    debugApi(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.paging)

    api(libs.androidx.compose.material)
    api(libs.androidx.compose.material.icons.extended)

    api(libs.accompanist.flowlayout)
    api(libs.accompanist.insets.ui)
    api(libs.accompanist.pager.indicators)
    api(libs.accompanist.systemuicontroller)
    api(libs.accompanist.swiperefresh)

    api(libs.coil.compose)

    api(libs.lottie)

    api(libs.material)
}
