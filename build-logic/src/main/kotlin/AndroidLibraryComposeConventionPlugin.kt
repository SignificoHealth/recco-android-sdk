import com.android.build.gradle.LibraryExtension
import com.recco.sdk.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("recco.android.library")

            extensions.configure<LibraryExtension> {
                configureAndroidCompose(this)
            }
        }
    }
}
