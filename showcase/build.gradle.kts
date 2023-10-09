import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("recco.android.spotless")

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")

    alias(libs.plugins.easylauncher)
}

android {
    namespace = "com.recco.showcase"

    compileSdk = 34

    defaultConfig {
        applicationId = "com.recco.showcase"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    val appIdSuffix = resolveAppIdSuffix()
    val clientSecret = resolveClientSecret()

    buildTypes {
        named("debug") {
            applicationIdSuffix = ".$appIdSuffix"

            buildConfigField(
                type = "String",
                name = "CLIENT_SECRET",
                value = clientSecret
            )
        }


        named("release") {
            applicationIdSuffix = ".$appIdSuffix"

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
                value = clientSecret
            )
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
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {
    implementation(project(":api:ui"))

    // implementation("com.significo:recco-api-ui:0.0.0")

    implementation("com.google.dagger:hilt-android:2.48.1")
    implementation("androidx.navigation:navigation-compose:2.7.4")

    ksp("com.google.dagger:hilt-android-compiler:2.48.1")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")

    implementation(platform("androidx.compose:compose-bom:2023.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    implementation("com.github.viluahealthcare:compose-html:1.0.3")

    implementation("androidx.compose.ui:ui-text-google-fonts:1.5.3")

    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    implementation("com.github.skydoves:colorpicker-compose:1.0.4")
    implementation("com.holix.android:bottomsheetdialog-compose:1.2.2")

    val roomVersion = "2.5.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    //debugImplementation("androidx.compose.ui:ui-tooling:1.6.0-alpha03")
}

easylauncher {
    buildTypes {
        val appIdSuffix = resolveAppIdSuffix().uppercase()

        create("debug") {
            filters(
                customRibbon(
                    label = appIdSuffix,
                    ribbonColor = "#FFf5b731",
                    labelColor = "#FFFFFF"
                )
            )
        }

        create("release") {
            filters(
                customRibbon(
                    label = appIdSuffix,
                    ribbonColor = "#FF9EE4A1",
                    labelColor = "#FFFFFF"
                )
            )
        }
    }
}

fun resolveAppIdSuffix(): String {
    val applicationIdSuffixEnv = System.getenv("RECCO_APP_ID_SUFFIX")
    val applicationIdLocalProperties: String? = gradleLocalProperties(rootDir).getProperty("recco.app.id.suffix")
    return applicationIdSuffixEnv ?: applicationIdLocalProperties ?: "dev"
}

fun resolveClientSecret(): String {
    val reccoClientSecretEnv: String? = System.getenv("RECCO_CLIENT_SECRET")
    val reccoClientSecretLocalProperties: String? = gradleLocalProperties(rootDir).getProperty("recco.client.secret")
    // Default to empty to avoid crashing the build when running status checks on Github Actions such as Lint
    return reccoClientSecretEnv ?: reccoClientSecretLocalProperties ?: "\"\""
}
