import com.android.build.gradle.LibraryExtension
import com.shadowflight.sdk.configureKotlinAndroid
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.the

@Suppress("unused")
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }

            val libs = the<LibrariesForLibs>()

            dependencies {
                "implementation"(libs.kotlinx.coroutines.android)

                "implementation"(libs.androidx.annotation)
                "implementation"(libs.androidx.core)
            }
        }
    }
}
