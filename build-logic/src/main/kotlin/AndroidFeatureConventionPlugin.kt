import com.android.build.gradle.LibraryExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.the

@Suppress("unused")
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("recco.android.library")
                apply("recco.android.library.compose")
                apply("recco.android.hilt")
            }

            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }

            val libs = the<LibrariesForLibs>()

            dependencies {
                "implementation"(project(":internal:core:ui"))

                "implementation"(libs.androidx.lifecycle.runtime.compose)
                "implementation"(libs.androidx.lifecycle.viewmodel.compose)

                "implementation"(libs.accompanist.navigation.animation)
                "implementation"(libs.accompanist.navigation.material)

                "testImplementation"(project(":internal:core:test"))
            }
        }
    }
}
