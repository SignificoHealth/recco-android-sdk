plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("recco.android.spotless")

    kotlin("kapt")
    id("com.google.dagger.hilt.android")

    alias(libs.plugins.easylauncher)
}

android {
    namespace = "com.recco.showcase"

    compileSdk = 33

    defaultConfig {
        applicationId = "com.recco.showcase"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        named("debug") {
            keyAlias = "debug"
            keyPassword = "password"
            storeFile = file("${project.projectDir}/keys/debug.keystore")
            storePassword = "password"
        }
        create("release") {
            keyAlias = "release"
            keyPassword = "password"
            storeFile = file("${project.projectDir}/keys/release.keystore")
            storePassword = "password"
        }
    }

    buildTypes {
        named("debug") {
            applicationIdSuffix = ".debug"

            buildConfigField(
                type = "String",
                name = "CLIENT_SECRET",
                value = "\"ax56dFyYSU7fgnJU6PuNyWSJ_rFQccFSYr16t3kj11J9NzelobNNRxBvBpFh97ZszHPjjnE\""
            )

            signingConfig = signingConfigs.getByName("debug")
        }

        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )

            buildConfigField(
                type = "String",
                name = "CLIENT_SECRET",
                value = "\"_20_0iKompsh-xy0Pc2YAYLHFimFV0qCZVSPobB982K1dIhhkYwbhutnBsSQsNqV0aludu8\""
            )

            signingConfig = signingConfigs.getByName("release")
        }
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.8"
    }
}

dependencies {
    implementation(project(":api:ui"))

    // implementation("com.significo:recco-api-ui:0.0.0")

    implementation("com.google.dagger:hilt-android:2.47")
    implementation("androidx.navigation:navigation-compose:2.6.0")

    kapt("com.google.dagger:hilt-android-compiler:2.47")

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")

    implementation(platform("androidx.compose:compose-bom:2023.06.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    implementation("com.github.viluahealthcare:compose-html:1.0.3")

    implementation("androidx.compose.ui:ui-text-google-fonts:1.4.3")
}

easylauncher {
    buildTypes {
        create("debug") {
            filters(
                customRibbon(
                    label = "DEBUG",
                    ribbonColor = "#FFf5b731",
                    labelColor = "#FFFFFF"
                )
            )
        }

        create("release") {
            filters(
                customRibbon(
                    label = "PROD",
                    ribbonColor = "#FF9EE4A1",
                    labelColor = "#FFFFFF"
                )
            )
        }
    }
}

