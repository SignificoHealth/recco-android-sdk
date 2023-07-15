package com.recco.sdk

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.register

/**
 * Project.configureMavenPublish
 *
 * Allows configuring both debug and release buildTypes to be published using Maven Plugin.
 *
 * @param [PublishingExtension] extension configuration of how to publish components.
 */
internal fun Project.configureMavenPublish(extension: PublishingExtension) {

    val debugEnv = "debug"
    val releaseEnv = "release"

    // Delegated properties from gradle.properties file in each module for the sake of flexibility.
    val moduleGroupId: String by project
    val moduleArtifactId: String by project
    val moduleVersion: String by project

    extension.apply {

        repositories {
            maven {
                name = "GithubPackages"
                url = uri("https://maven.pkg.github.com/sf-recco/android-sdk")
                credentials {
                    username = System.getenv("USERNAME")
                    password = System.getenv("TOKEN")
                }
            }
        }

        afterEvaluate {
            publications {

                // Debug
                register<MavenPublication>(debugEnv) {
                    groupId = moduleGroupId
                    artifactId = moduleArtifactId
                    version = moduleVersion

                    afterEvaluate {
                        from(components[debugEnv])
                    }
                }

                // Release
                register<MavenPublication>(releaseEnv) {
                    groupId = moduleGroupId
                    artifactId = moduleArtifactId
                    version = moduleVersion

                    afterEvaluate {
                        from(components[releaseEnv])
                    }
                }
            }
        }
    }
}

/**
 * Project.configureMavenPublish
 *
 * Allows publishing sources & javadoc jar apart from AAR.
 *
 * @param [LibraryExtension] extension android block when the com.android.library plugin is applied.
 */
internal fun configureDocsAndSources(extension: LibraryExtension) {
    extension.apply {
        publishing {
            multipleVariants {
                allVariants()
                withSourcesJar()
                withJavadocJar()
            }
        }
    }
}