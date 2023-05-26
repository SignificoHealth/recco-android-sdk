plugins {
    id("shadowflight.android.library")
    alias(libs.plugins.ksp)
    alias(libs.plugins.openapi)
}

android {
    namespace = "com.shadowflight.openapi"
}

dependencies {
    implementation(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)

    implementation(libs.sandwich)
}

openApiGenerate {
    inputSpec.set("$rootDir/openapi/openapi.json")
    outputDir.set("$rootDir/openapi")
    configFile.set("$rootDir/openapi/openapi-generator-config.json")
    templateDir.set("$rootDir/openapi/templates")
    globalProperties.set(
        mapOf(
            "supportingFiles" to "false",
            "apis" to "",
            "models" to "",
            "apiDocs" to "false",
            "modelDocs" to "false"
        )
    )
}

tasks.named("openApiGenerate") {
    doFirst {
        delete(
            fileTree("$rootDir/openapi/src/main/kotlin/com/shadowflight/openapi/api"),
            fileTree("$rootDir/openapi/src/main/kotlin/com/shadowflight/openapi/model")
        )
    }
    doLast {
        delete(
            file("$rootDir/openapi/src/main/kotlin/com/shadowflight/openapi/model/java.time.LocalTime.kt")
        )
    }
}
