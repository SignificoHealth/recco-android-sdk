package com.shadowflight.sdk

import com.android.build.api.dsl.CommonExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.the

internal fun Project.configureAndroidCompose(extension: CommonExtension<*, *, *, *>) {
    val libs = the<LibrariesForLibs>()

    extension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
        }

        dependencies {
            val bom = platform(libs.androidx.compose.bom)
            "implementation"(bom)
        }
    }
}
