import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties


plugins {
    id("recco.android.library")
    id("recco.android.hilt")
    id("recco.android.spotless")
}

android {
    namespace = "com.recco.internal.core.network"

    val reccoBaseUrlLocalProperties: String? = gradleLocalProperties(rootDir).getProperty("recco.base.url")
    val reccoBaseUrlEnv: String? = System.getenv("RECCO_BASE_URL")
    val baseUrl = reccoBaseUrlLocalProperties ?: reccoBaseUrlEnv ?: "\"https://api.sf-dev.significo.dev/\""

    buildTypes {
        debug {
            buildConfigField(
                type = "String",
                name = "RECCO_BASE_URL",
                value = baseUrl
            )
        }
        release {
            buildConfigField(
                type = "String",
                name = "RECCO_BASE_URL",
                value = baseUrl
            )
        }
    }
}

dependencies {
    implementation(project(":api:model"))
    implementation(project(":internal:core:base"))
    implementation(project(":internal:core:model"))
    implementation(project(":internal:core:persistence"))
    implementation(project(":internal:core:openapi"))
    implementation(project(":internal:core:logger"))

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    api(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)

    implementation(libs.moshi)
}


