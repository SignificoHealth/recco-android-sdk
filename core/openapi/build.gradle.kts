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
}

private val pathOpenAPI = "$rootDir/core/openapi"

openApiGenerate {
    inputSpec.set("$pathOpenAPI/openapi.json")
    outputDir.set(pathOpenAPI)
    configFile.set("$pathOpenAPI/openapi-generator-config.json")
    templateDir.set("$pathOpenAPI/templates")
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
            fileTree("$pathOpenAPI/src/main/kotlin/com/shadowflight/openapi/api"),
            fileTree("$pathOpenAPI/src/main/kotlin/com/shadowflight/openapi/model")
        )
    }
    doLast {
        delete(
            file("$pathOpenAPI/src/main/kotlin/com/shadowflight/openapi/model/java.time.LocalTime.kt")
        )
    }
}
