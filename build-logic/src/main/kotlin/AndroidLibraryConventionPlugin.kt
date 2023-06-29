import com.android.build.gradle.LibraryExtension
import com.recco.sdk.configureKotlinAndroid
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
                apply("recco.android.maven.publish")
                apply("de.mannodermaus.android-junit5")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }

            val libs = the<LibrariesForLibs>()

            dependencies {
                "implementation"(libs.kotlinx.coroutines.android)
                "implementation"(libs.androidx.annotation)
                "implementation"(libs.androidx.core)

                "testImplementation"(libs.junit.api)
                "testRuntimeOnly"(libs.junit.engine)
                "testImplementation"(libs.mockito)
                "testImplementation"(libs.coroutines.test)
            }
        }
    }
}
