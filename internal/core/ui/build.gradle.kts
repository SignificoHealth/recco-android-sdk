plugins {
    id("recco.android.library.compose")
    alias(libs.plugins.kotlin.serialization)
}

android {
    // Changed namespace due to inconsistency building release variant
    namespace = "com.recco.internal.core.ui"
}

dependencies {
    implementation(project(":internal:core:model"))
    implementation(libs.kotlinx.serialization.json)

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
    api(libs.accompanist.drawablepainter)

    api(libs.coil.compose)

    api(libs.lottie)

    api(libs.material)

    api(libs.androidx.hilt.navigation.compose)
    api(libs.androidx.navigation.compose)

    api(libs.compose.html)
}