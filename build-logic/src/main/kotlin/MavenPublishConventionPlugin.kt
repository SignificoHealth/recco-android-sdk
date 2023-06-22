import com.android.build.api.dsl.LibraryExtension
import com.recco.sdk.configureDocsAndSources
import com.recco.sdk.configureMavenPublish
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class MavenPublishConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            pluginManager.apply{
                apply("maven-publish")
            }

            extensions.configure<LibraryExtension> {
                configureDocsAndSources(this)
            }

            extensions.configure<PublishingExtension> {
                configureMavenPublish(this)
            }
        }
    }
}