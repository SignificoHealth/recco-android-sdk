import dagger.hilt.android.plugin.HiltExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

@Suppress("unused")
class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.google.dagger.hilt.android")
                apply("org.jetbrains.kotlin.kapt")
            }

            val libs = the<LibrariesForLibs>()

            dependencies {
                "implementation"(libs.hilt.android)
                "kapt"(libs.hilt.compiler)
                "kaptAndroidTest"(libs.hilt.compiler)
            }

            extensions.configure<KaptExtension> {
                correctErrorTypes = true
            }

            extensions.configure<HiltExtension> {
                enableAggregatingTask = true
            }
        }
    }
}
